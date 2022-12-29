package kr.icetang0123.durnotify.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import kr.icetang0123.durnotify.client.utils.Util;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ModMenuIntegration implements ModMenuApi {
    ConfigSerializer serializer = new ConfigSerializer();
    AtomicReference<String> warnMessageValue;
    AtomicReference<String> warnArmorMessageValue;
    AtomicReference<Boolean> warnActionBarValue;
    AtomicReference<Integer> warnMaxUnder50;
    AtomicReference<Integer> warnMaxUnder100;
    AtomicReference<Integer> warnMaxUnder200;
    AtomicReference<Integer> warnMaxAbove200;

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("title.durnotify.config"));

            ConfigCategory messagesCategory = builder.getOrCreateCategory(Text.translatable("title.durnotify.config_messages"));
            ConfigCategory constantsCategory = builder.getOrCreateCategory(Text.translatable("title.durnotify.config_constants"));

            Map<String, Object> config = serializer.load();

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            warnMessageValue = new AtomicReference<>((String) config.get("warn_message"));

            messagesCategory.addEntry(
                    entryBuilder.startStrField(Text.translatable("config.durnotify.warn_message"), warnMessageValue.get())
                        .setDefaultValue("Low dur!")
                        .setTooltip(Text.translatable("tooltip.durnotify.warn_message"))
                        .setSaveConsumer(warnMessageValue::set)
                    .build()
            );

            warnArmorMessageValue = new AtomicReference<>((String) config.get("warn_armor_message"));

            messagesCategory.addEntry(
                    entryBuilder.startStrField(Text.translatable("config.durnotify.warn_armor_message"), warnArmorMessageValue.get())
                        .setDefaultValue("Low armor dur!")
                        .setTooltip(Text.translatable("tooltip.durnotify.warn_armor_message"))
                        .setSaveConsumer(warnArmorMessageValue::set)
                    .build()
            );

            warnActionBarValue = new AtomicReference<>((Boolean) config.get("warn_into_actionbar"));

            messagesCategory.addEntry(
                    entryBuilder.startBooleanToggle(Text.translatable("config.durnotify.warn_into_actionbar"), warnActionBarValue.get())
                            .setDefaultValue(true)
                            .setTooltip(Text.translatable("tooltip.durnotify.warn_into_actionbar.line1"), Text.translatable("tooltip.durnotify.warn_into_actionbar.line2"))
                            .setSaveConsumer(warnActionBarValue::set)
                            .build()
            );

            warnMaxUnder50 = new AtomicReference<>(Util.integerOrDouble(config.get("warn_max_under_50")));


            constantsCategory.addEntry(
                    entryBuilder.startIntSlider(Text.translatable("config.durnotify.warn_max_under_50"), warnMaxUnder50.get(), 1, 50)
                            .setDefaultValue(15)
                            .setTooltip(Text.translatable("tooltip.durnotify.warn_max_under_50"))
                            .setSaveConsumer(warnMaxUnder50::set)
                            .build()
            );

            warnMaxUnder100 = new AtomicReference<>(Util.integerOrDouble(config.get("warn_max_under_100")));

            constantsCategory.addEntry(
                    entryBuilder.startIntSlider(Text.translatable("config.durnotify.warn_max_under_100"), warnMaxUnder100.get(), 1, 100)
                            .setDefaultValue(50)
                            .setTooltip(Text.translatable("tooltip.durnotify.warn_max_under_100"))
                            .setSaveConsumer(warnMaxUnder100::set)
                            .build()
            );

            warnMaxUnder200 = new AtomicReference<>(Util.integerOrDouble(config.get("warn_max_under_200")));

            constantsCategory.addEntry(
                    entryBuilder.startIntSlider(Text.translatable("config.durnotify.warn_max_under_200"), warnMaxUnder200.get(), 1, 200)
                            .setDefaultValue(65)
                            .setTooltip(Text.translatable("tooltip.durnotify.warn_max_under_200"))
                            .setSaveConsumer(warnMaxUnder200::set)
                            .build()
            );

            warnMaxAbove200 = new AtomicReference<>(Util.integerOrDouble(config.get("warn_max_above_200")));

            constantsCategory.addEntry(
                    entryBuilder.startIntField(Text.translatable("config.durnotify.warn_max_above_200"), warnMaxAbove200.get())
                            .setDefaultValue(75)
                            .setMin(1)
                            .setTooltip(Text.translatable("tooltip.durnotify.warn_max_above_200"))
                            .setSaveConsumer(warnMaxAbove200::set)
                            .build()
            );

            /*
            constantsCategory.addEntry(new NestedListListEntry<Pair<Item, Integer>, MultiElementListEntry<Pair<Item, Integer>>>(
                    Text.literal("Nice"),
                    Lists.newArrayList(new Pair<>(Items.APPLE, 10)),
                    false,
                    Optional::empty,
                    list -> {},
                    Lists::newArrayList,
                    entryBuilder.getResetButtonKey(),
                    true,
                    true,
                    (elem, nestedListListEntry) -> {
                        if (elem != null) {
                            return new MultiElementListEntry<>(Text.literal("Pair"), elem,
                                    Lists.newArrayList(entryBuilder.startDropdownMenu(Text.literal("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.BOW), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setErrorSupplier(item -> {
                                        if (!item.isDamageable()) return Optional.of(Text.of("Invalid value!"));
                                        return Optional.empty();
                                    }).setDefaultValue(Items.BOW).setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).filter(Item::isDamageable).collect(Collectors.toCollection(LinkedHashSet::new))).build(),
                                            entryBuilder.startIntField(Text.literal("Right"), elem.getRight()).setDefaultValue(10).build()),
                                    true);
                        } else return new MultiElementListEntry<>(Text.literal("Pair"), new Pair<>(null, null),
                                Lists.newArrayList(entryBuilder.startDropdownMenu(Text.literal("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.BOW), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.BOW).setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).filter(Item::isDamageable).collect(Collectors.toCollection(LinkedHashSet::new))).build(),
                                        entryBuilder.startIntField(Text.literal("Right"), 0).setDefaultValue(10).build()),
                                true);
                    }
            ));
             */

            builder.setSavingRunnable(() ->
                    serializer.save(this)
            );

            return builder.build();
        };
    }
}