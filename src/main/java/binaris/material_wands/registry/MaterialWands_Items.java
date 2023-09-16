package binaris.material_wands.registry;

import binaris.material_wands.MaterialWands;
import binaris.material_wands.util.WandBuilder;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MaterialWands_Items {
    public static Item BLACKSTONE_WAND = registerItem("blackstone_wand", new WandBuilder(Blocks.BLACKSTONE, Blocks.COBBLESTONE, Items.COBBLESTONE, Items.BLACKSTONE, true, WandBuilder.WAND_TYPE.CONVERTOR));
    public static Item DEEPSLATE_WAND = registerItem("deepslate_wand", new WandBuilder(Blocks.COBBLED_DEEPSLATE, Blocks.COBBLESTONE, Items.COBBLESTONE, Items.COBBLED_DEEPSLATE, true, WandBuilder.WAND_TYPE.CONVERTOR));
    public static Item SAND_WAND = registerItem("sand_wand", new WandBuilder(Blocks.RED_SAND, Blocks.SAND, Items.RED_SAND, Items.SAND, true, WandBuilder.WAND_TYPE.CONVERTOR));
    public static Item COPPER_WAND = registerItem("copper_wand", new WandBuilder(Blocks.COPPER_BLOCK, Blocks.OXIDIZED_COPPER, Items.COPPER_BLOCK, Items.OXIDIZED_COPPER, true, WandBuilder.WAND_TYPE.CONVERTOR));


    public static Item BONE_WAND = registerItem("bone_wand", new WandBuilder(Blocks.BONE_BLOCK, null, null,null, false, WandBuilder.WAND_TYPE.GENERATOR));


    public static final ItemGroup GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(MaterialWands.MOD_ID, "material_wands"),
            FabricItemGroup.builder().displayName(Text.literal("Material Wands"))
                    .icon(() -> new ItemStack(BLACKSTONE_WAND)).entries((displayContext, entries) -> {
                        entries.add(BLACKSTONE_WAND);
                        entries.add(DEEPSLATE_WAND);
                        entries.add(COPPER_WAND);
                        entries.add(SAND_WAND);

                        entries.add(BONE_WAND);
                    }).build());
    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(MaterialWands.MOD_ID, name), item);
    }

    public static void registerAllItems(){

    }
}
