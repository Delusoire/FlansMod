package co.uk.flansmods.common;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/** Implemented from old source. */
public class ItemBullet extends Item
{
	public ItemBullet(int i, int j, int k, BulletType type1)
	{
		super(i);
		setIconIndex(j);
		colour = k;
		type = type1;
		setMaxDamage(type.roundsPerItem);
		setMaxStackSize(type.maxStackSize);
		type.item = this;
		setCreativeTab(CreativeTabs.tabCombat);
	}

	public String getTextureFile()
	{
		return "/spriteSheets/bullets.png";
	}

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
    	return type.colour;
    }

	public int colour;
	public BulletType type;
}