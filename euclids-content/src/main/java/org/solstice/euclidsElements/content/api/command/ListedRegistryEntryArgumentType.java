package org.solstice.euclidsElements.content.api.command;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtString;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public abstract class ListedRegistryEntryArgumentType<T> implements ArgumentType<RegistryEntry<T>> {

	private static final SimpleCommandExceptionType INVALID_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("argument.resource_or_id.invalid"));
	private final RegistryWrapper.WrapperLookup registryLookup;
	private final boolean canLookupRegistry;
	private final Codec<RegistryEntry<T>> entryCodec;
	protected final RegistryKey<? extends Registry<T>> key;

	protected ListedRegistryEntryArgumentType(CommandRegistryAccess registryAccess, RegistryKey<Registry<T>> registry, Codec<RegistryEntry<T>> entryCodec) {
		this.registryLookup = registryAccess;
		this.canLookupRegistry = registryAccess.getOptionalWrapper(registry).isPresent();
		this.entryCodec = entryCodec;
		this.key = registry;
	}

	@SuppressWarnings("unchecked")
	protected static <T> RegistryEntry<T> getArgument(CommandContext<ServerCommandSource> context, String argument) {
		return (RegistryEntry<T>) context.getArgument(argument, RegistryEntry.class);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return context.getSource() instanceof CommandSource commandSource
			? commandSource.listIdSuggestions(this.key, CommandSource.SuggestedIdType.ELEMENTS, builder, context)
			: builder.buildFuture();
	}

	@Override
	@Nullable
	public RegistryEntry<T> parse(StringReader stringReader) throws CommandSyntaxException {
		NbtElement nbtElement = parseAsNbt(stringReader);
		if (!this.canLookupRegistry) return null;

		RegistryOps<NbtElement> registryOps = this.registryLookup.getOps(NbtOps.INSTANCE);
		return this.entryCodec.parse(registryOps, nbtElement).getOrThrow((argument) ->
			RegistryEntryArgumentType.FAILED_TO_PARSE_EXCEPTION.createWithContext(stringReader, argument)
		);
	}

	@VisibleForTesting
	static NbtElement parseAsNbt(StringReader stringReader) throws CommandSyntaxException {
		int i = stringReader.getCursor();
		NbtElement nbtElement = (new StringNbtReader(stringReader)).parseElement();
		if (hasFinishedReading(stringReader)) {
			return nbtElement;
		} else {
			stringReader.setCursor(i);
			Identifier identifier = Identifier.fromCommandInput(stringReader);
			if (hasFinishedReading(stringReader)) {
				return NbtString.of(identifier.toString());
			} else {
				stringReader.setCursor(i);
				throw INVALID_EXCEPTION.createWithContext(stringReader);
			}
		}
	}

	private static boolean hasFinishedReading(StringReader stringReader) {
		return !stringReader.canRead() || stringReader.peek() == ' ';
	}

}
