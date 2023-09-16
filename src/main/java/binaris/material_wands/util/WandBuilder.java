package binaris.material_wands.util;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class WandBuilder extends Item {
    public final Block block;
    public final Block block1;
    @Nullable
    public final Item item;
    public final Item item1;
    private final WAND_TYPE type;
    public boolean mode = true;
    public boolean enableMode;

    public WandBuilder(Block block, Block block1, @Nullable Item item, @Nullable Item item1, boolean changeMode, WAND_TYPE wand_type) {
        super(new FabricItemSettings().maxCount(1));
        this.block = block;
        this.block1 = block1;
        this.item = item;
        this.item1 = item1;
        this.enableMode = changeMode;
        this.type = wand_type;
    }

    // All this code is literally a copy and paste from BlockItem :>
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerInventory inventory = context.getPlayer().getInventory();

        if(this.type == WAND_TYPE.GENERATOR){return place(new ItemPlacementContext(context), block, null);}
        else if(inventory.contains(item.getDefaultStack()) && this.mode && this.type == WAND_TYPE.CONVERTOR){return place(new ItemPlacementContext(context), block, item);}
        else if(inventory.contains(item1.getDefaultStack()) && !this.mode && this.type == WAND_TYPE.CONVERTOR) {return place(new ItemPlacementContext(context), block1, item1);}

        else{
            return ActionResult.FAIL;
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user.isSneaking() && world.getTime() % 20 == 0){
            if(this.mode){
                this.mode = false;
                user.sendMessage(Text.literal(item1.getName().getString()).formatted(Formatting.AQUA).append(" -> ").append(block1.getName().getString()), true);
            }
            else{
                this.mode = true;
                user.sendMessage(Text.literal(item.getName().getString()).formatted(Formatting.AQUA).append(" -> ").append(block.getName().getString()), true);
            }
        }
        return super.use(world, user, hand);
    }

    public ActionResult place(ItemPlacementContext context, Block principal_block, @Nullable Item requested_item) {
        if (!principal_block.isEnabled(context.getWorld().getEnabledFeatures())) {
            return ActionResult.FAIL;
        }
        else if (!context.canPlace()) {
            return ActionResult.FAIL;
        }

        else {
            BlockState blockState = this.getPlacementState(context, principal_block);
            if (blockState == null) {
                return ActionResult.FAIL;
            } else if (!this.place(context, blockState)) {
                return ActionResult.FAIL;
            } else {
                BlockPos blockPos = context.getBlockPos();
                World world = context.getWorld();
                PlayerEntity playerEntity = context.getPlayer();
                ItemStack itemStack = context.getStack();
                BlockState blockState2 = world.getBlockState(blockPos);


                if (blockState2.isOf(blockState.getBlock())) {

                    blockState2.getBlock().onPlaced(world, blockPos, blockState2, playerEntity, itemStack);
                    if (playerEntity instanceof ServerPlayerEntity) {
                        Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
                    }
                }

                BlockSoundGroup blockSoundGroup = blockState2.getSoundGroup();
                world.playSound(playerEntity, blockPos, this.getPlaceSound(blockState2), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
                world.emitGameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Emitter.of(playerEntity, blockState2));

                if(!context.getPlayer().isCreative() && this.type == WAND_TYPE.CONVERTOR) {
                    PlayerInventory inventory = context.getPlayer().getInventory();

                    inventory.removeStack(inventory.getSlotWithStack(requested_item.getDefaultStack()), 1);
                }
                return ActionResult.success(world.isClient);
            }
        }
    }

    protected SoundEvent getPlaceSound(BlockState state) {
        return state.getSoundGroup().getPlaceSound();
    }
    @Nullable
    protected BlockState getPlacementState(ItemPlacementContext context, Block block) {
        BlockState blockState = block.getPlacementState(context);
        return blockState != null && canPlace(context, blockState) ? blockState : null;
    }

    protected boolean canPlace(ItemPlacementContext context, BlockState state) {
        PlayerEntity playerEntity = context.getPlayer();
        ShapeContext shapeContext = playerEntity == null ? ShapeContext.absent() : ShapeContext.of(playerEntity);
        return (!this.checkStatePlacement() || state.canPlaceAt(context.getWorld(), context.getBlockPos())) && context.getWorld().canPlace(state, context.getBlockPos(), shapeContext);
    }

    protected boolean checkStatePlacement() {
        return true;
    }

    protected boolean place(ItemPlacementContext context, BlockState state) {
        return context.getWorld().setBlockState(context.getBlockPos(), state, 11);
    }

    public enum WAND_TYPE {
        GENERATOR,
        CONVERTOR
    }
}
