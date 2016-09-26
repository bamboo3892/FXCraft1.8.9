package com.okina.fxcraft.main;

import static com.okina.fxcraft.main.FXCraft.*;

import java.io.File;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.okina.fxcraft.account.AccountHandler;
import com.okina.fxcraft.account.AccountInfo;
import com.okina.fxcraft.account.Reward.Rewards;
import com.okina.fxcraft.account.RewardRegister;
import com.okina.fxcraft.block.BlockAccountManager;
import com.okina.fxcraft.block.BlockEternalStorage;
import com.okina.fxcraft.block.BlockFXDealer;
import com.okina.fxcraft.item.ItemBlockEternalStorage;
import com.okina.fxcraft.item.ItemCapitalistGuard;
import com.okina.fxcraft.item.ItemCapitalistGun;
import com.okina.fxcraft.item.ItemFXMask;
import com.okina.fxcraft.item.ItemIPhone;
import com.okina.fxcraft.item.ItemJentlemensCap;
import com.okina.fxcraft.item.ItemJentlemensPanz;
import com.okina.fxcraft.item.ItemMetaBlock;
import com.okina.fxcraft.item.ItemToolTip;
import com.okina.fxcraft.network.CommandPacket;
import com.okina.fxcraft.network.CommandPacket.CommandPacketHandler;
import com.okina.fxcraft.network.SimpleTilePacket;
import com.okina.fxcraft.network.SimpleTilePacket.SimpleTilePacketHandler;
import com.okina.fxcraft.network.SimpleTilePacket.SimpleTileReplyPacketHandler;
import com.okina.fxcraft.tileentity.AccountManegerTileEntity;
import com.okina.fxcraft.tileentity.EternalStorageEnergyTileEntity;
import com.okina.fxcraft.tileentity.EternalStorageFluidTileEntity;
import com.okina.fxcraft.tileentity.EternalStorageItemTileEntity;
import com.okina.fxcraft.tileentity.FXDealerTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

	protected void loadConfiguration(File pfile) {
		//		Configuration config = new Configuration(pfile);
		//		try{
		//			config.load();
		//			config.getInt("Particle Level", "EFFECT", 3, 0, 3, "Now this configulation replaced to proterty file.");
		//		}catch (Exception e){
		//			FMLLog.severe("config load errer");
		//		}finally{
		//			config.save();
		//		}
		AccountHandler.instance.readFromFile();
	}

	protected void registerBlock() {
		accountManager = new BlockAccountManager();
		registerBlock(accountManager);
		fxDealer = new BlockFXDealer();
		registerBlock(fxDealer);
		eternalStorage = new BlockEternalStorage();
		String name = eternalStorage.getUnlocalizedName().substring(5);
		GameRegistry.registerBlock(eternalStorage, ItemBlockEternalStorage.class, name);
		if(FMLCommonHandler.instance().getSide().isClient()){
			ModelBakery.addVariantName(Item.getItemFromBlock(eternalStorage), MODID + ":" + name + "_item", MODID + ":" + name + "_energy", MODID + ":" + name + "_fluid");
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(eternalStorage), 0, new ModelResourceLocation(MODID + ":" + name + "_item", "invenotry"));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(eternalStorage), 1, new ModelResourceLocation(MODID + ":" + name + "_energy", "invenotry"));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(eternalStorage), 2, new ModelResourceLocation(MODID + ":" + name + "_fluid", "invenotry"));
		}

		//		test = new TestBlock();
		//		GameRegistry.registerBlock(test, ItemMetaBlock.class, "test_block");
		//		if(FMLCommonHandler.instance().getSide().isClient()){
		//			ModelBakery.addVariantName(Item.getItemFromBlock(test), MODID + ":test_block0", MODID + ":test_block1");
		//			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(test), 0, new ModelResourceLocation(MODID + ":test_block0", "invenotry"));
		//			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(test), 1, new ModelResourceLocation(MODID + ":test_block1", "invenotry"));
		//		}
	}

	private void registerBlock(Block block) {
		registerBlock(block, ItemMetaBlock.class);
	}

	private void registerBlock(Block block, Class<? extends ItemBlock> itemBlock) {
		String name = block.getUnlocalizedName().substring(5);
		GameRegistry.registerBlock(block, itemBlock, name);
		if(FMLCommonHandler.instance().getSide().isClient()){
			ModelBakery.addVariantName(Item.getItemFromBlock(block), MODID + ":" + name);
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(MODID + ":" + name, "invenotry"));
		}
	}

	protected void registerItem() {
		iPhone = new ItemIPhone();
		registerItem(iPhone);

		ArmorMaterial panz = EnumHelper.addArmorMaterial("panz", "", 0, new int[] { 0, 0, 0, 0 }, 0);
		ArmorMaterial emeral = EnumHelper.addArmorMaterial("emerald", "", 0, new int[] { 0, 0, 0, 0 }, 0);
		jentlemens_cap = new ItemJentlemensCap(panz, 1);
		registerItem(jentlemens_cap);
		jentlemens_panz = new ItemJentlemensPanz(panz, 1);
		registerItem(jentlemens_panz);
		capitalist_gun = new ItemCapitalistGun();
		registerItem(capitalist_gun);
		capitalist_guard = new ItemCapitalistGuard(emeral, 1);
		registerItem(capitalist_guard);
		fx_mask = new ItemFXMask(panz, 1);
		registerItem(fx_mask);

		for (int i = 0; i < 5; i++){
			limit_dealLot[i] = new ItemToolTip(Lists.newArrayList("Permit to deal " + AccountInfo.DEAL_LIMIT[i + 1] + " lot or less")).setUnlocalizedName("fxcraft_limit_dealLot_" + (i + 1)).setCreativeTab(FXCraftCreativeTab);
			registerItem(limit_dealLot[i]);
		}
		for (int i = 0; i < 5; i++){
			limit_leverage[i] = new ItemToolTip(Lists.newArrayList("Permit to deal by leverage " + AccountInfo.LEVERAGE_LIMIT[i + 1] + ".0 or less")).setUnlocalizedName("fxcraft_limit_leverage_" + (i + 1)).setCreativeTab(FXCraftCreativeTab);
			registerItem(limit_leverage[i]);
		}
		for (int i = 0; i < 5; i++){
			limit_position[i] = new ItemToolTip(Lists.newArrayList("Permit to get " + AccountInfo.POSITION_LIMIT[i + 1] + " positions or less")).setUnlocalizedName("fxcraft_limit_position_" + (i + 1)).setCreativeTab(FXCraftCreativeTab);
			registerItem(limit_position[i]);
		}
		limit_limits_trade = new ItemToolTip(Lists.newArrayList("Permit to trade with limits")).setUnlocalizedName("fxcraft_limit_limits_trade").setCreativeTab(FXCraftCreativeTab);
		registerItem(limit_limits_trade);

		for (int i = 0; i < Rewards.TOTAL_DEAL.length; i++){
			RewardRegister.instance.registerReward(Rewards.TOTAL_DEAL[i]);
		}
		for (int i = 0; i < Rewards.TOTAL_GAIN.length; i++){
			RewardRegister.instance.registerReward(Rewards.TOTAL_GAIN[i]);
		}
		for (int i = 0; i < Rewards.TOTAL_LOSS.length; i++){
			RewardRegister.instance.registerReward(Rewards.TOTAL_LOSS[i]);
		}
		RewardRegister.instance.registerReward(Rewards.MAX_LOT);
		RewardRegister.instance.registerReward(Rewards.MAX_LEVERAGE);
		RewardRegister.instance.registerReward(Rewards.MAX_LOT_LEVERAGE);
		RewardRegister.instance.registerReward(Rewards.FIRST_DEAL);
		RewardRegister.instance.registerReward(Rewards.FIRST_LIMITS_DEAL);
		RewardRegister.instance.registerReward(Rewards.FIRST_LOSSCUT);
		RewardRegister.instance.registerFirstAimableReward(Rewards.FIRST_DEAL);
		RewardRegister.instance.registerFirstAimableReward(Rewards.TOTAL_DEAL[0]);
		RewardRegister.instance.registerFirstAimableReward(Rewards.TOTAL_GAIN[0]);
		RewardRegister.instance.registerFirstAimableReward(Rewards.TOTAL_LOSS[0]);
		RewardRegister.instance.registerFirstAimableReward(Rewards.FIRST_LIMITS_DEAL);
	}

	private void registerItem(Item item) {
		GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
		if(FMLCommonHandler.instance().getSide().isClient()){
			ModelBakery.addVariantName(item, MODID + ":" + item.getUnlocalizedName().substring(5));
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(MODID + ":" + item.getUnlocalizedName().substring(5), "invenotry"));
		}
	}

	protected void registerTileEntity() {
		GameRegistry.registerTileEntity(AccountManegerTileEntity.class, "AccountManegerTileEntity");
		GameRegistry.registerTileEntity(FXDealerTileEntity.class, "FXDealerTileEntity");
		GameRegistry.registerTileEntity(EternalStorageItemTileEntity.class, "EternalStorageItemTileEntity");
		GameRegistry.registerTileEntity(EternalStorageEnergyTileEntity.class, "EternalStorageEnergyTileEntity");
		GameRegistry.registerTileEntity(EternalStorageFluidTileEntity.class, "EternalStorageFluidTileEntity");
	}

	protected void registerRecipe() {
		GameRegistry.addRecipe(new ItemStack(accountManager), "SGS", "OSO", 'G', Blocks.glass_pane, 'S', Blocks.stone, 'O', Blocks.obsidian);
		GameRegistry.addRecipe(new ItemStack(fxDealer), "SES", "OSO", 'E', Items.emerald, 'S', Blocks.stone, 'O', Blocks.obsidian);
	}

	protected void registerRenderer() {}

	protected void registerPacket() {
		packetDispatcher.registerMessage(SimpleTilePacketHandler.class, SimpleTilePacket.class, SIMPLETILE_PACKET_ID, Side.SERVER);
		packetDispatcher.registerMessage(SimpleTileReplyPacketHandler.class, SimpleTilePacket.class, SIMPLETILE_REPLY_PACKET_ID, Side.CLIENT);
		packetDispatcher.registerMessage(CommandPacketHandler.class, CommandPacket.class, COMMAND_PACKET_ID, Side.CLIENT);
	}

	//file io//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void updatePropertyFile() {}

	protected void initFXThread() {
		rateGetter.init();
	}

	//packet//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//	private Map<PacketType, List<Position>> positionListMap = new HashMap<PacketType, List<Position>>();
	//
	//	/**return true if newly marked*/
	//	public boolean markForTileUpdate(Position position, PacketType type) {
	//		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
	//			if(positionListMap.get(type) != null){
	//				List<Position> positionList = positionListMap.get(type);
	//				for (Position tmp : positionList){
	//					if(tmp != null && tmp.equals(position)){
	//						//System.out.println("already marked update");
	//						return false;
	//					}
	//				}
	//				positionList.add(position);
	//			}else{
	//				List<Position> positionList = new ArrayList<Position>();
	//				positionList.add(position);
	//				positionListMap.put(type, positionList);
	//			}
	//			return true;
	//		}else{
	//			return false;
	//		}
	//	}
	//
	//	void sendAllUpdatePacket() {
	//		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
	//			List<SimpleTilePacket> packets = Lists.newArrayList();
	//			for (PacketType type : PacketType.values()){
	//				List<Position> positionList = positionListMap.get(type);
	//				if(positionList != null){
	//					for (Position position : positionList){
	//						TileEntity tile = MinecraftServer.getServer().getEntityWorld().getTileEntity(position.x, position.y, position.z);
	//						if(tile instanceof ISimpleTilePacketUser){
	//							SimpleTilePacket packet = ((ISimpleTilePacketUser) tile).getPacket(type);
	//							if(packet != null){
	//								//								packetDispatcher.sendToAll(packet);
	//								packets.add(packet);
	//							}
	//						}
	//					}
	//					positionList.clear();
	//				}
	//			}
	//			if(!packets.isEmpty()){
	//				WorldUpdatePacket packet = new WorldUpdatePacket(packets);
	//				packetDispatcher.sendToAll(packet);
	//			}
	//		}
	//	}

	protected List<SimpleTilePacket> serverPacketList = Collections.<SimpleTilePacket> synchronizedList(Lists.<SimpleTilePacket> newArrayList());

	public void sendPacketToClient(SimpleTilePacket packet) {
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
			packetDispatcher.sendToAll(packet);
		}else{
			serverPacketList.add(packet);
		}
	}

	public void sendPacketToServer(SimpleTilePacket packet) {}

	public void sendCommandPacket(CommandPacket packet, EntityPlayerMP player) {
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
			packetDispatcher.sendTo(packet, player);
		}
	}

	public void spawnParticle(World world, int id, double x, double y, double z, double vecX, double vecY, double vecZ) {}

	protected List<PopUpMessage> messageList = Collections.<PopUpMessage> synchronizedList(Lists.<PopUpMessage> newLinkedList());

	public void appendPopUp(String message) {
		packetDispatcher.sendToAll(new CommandPacket("message", message));
	}

	protected class PopUpMessage {
		protected String message;
		protected int liveTime;
		protected int index;

		protected PopUpMessage(String message, int liveTime, int index) {
			this.message = message;
			this.liveTime = liveTime;
			this.index = index;
		}
	}

}
