package com.flansmod.client.model;

import com.modularwarfare.client.fpp.basic.configs.ArmorRenderConfig;
import com.modularwarfare.client.model.ModelCustomArmor;
import com.modularwarfare.common.type.BaseType;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.client.model.ModelPlayer;

import com.flansmod.client.tmt.ModelRendererTurbo;
import com.flansmod.common.teams.ArmourType;

public class ModelCustomArmour extends com.modularwarfare.client.model.ModelCustomArmor
{
	public ArmourType type;

	public ModelRendererTurbo[] headModel = new ModelRendererTurbo[0];
	public ModelRendererTurbo[] bodyModel = new ModelRendererTurbo[0];
	public ModelRendererTurbo[] leftArmModel = new ModelRendererTurbo[0];
	public ModelRendererTurbo[] rightArmModel = new ModelRendererTurbo[0];
	public ModelRendererTurbo[] leftLegModel = new ModelRendererTurbo[0];
	public ModelRendererTurbo[] rightLegModel = new ModelRendererTurbo[0];
	public ModelRendererTurbo[] skirtFrontModel = new ModelRendererTurbo[0]; //Acts like a leg piece, but its pitch is set to the maximum of the two legs
	public ModelRendererTurbo[] skirtRearModel = new ModelRendererTurbo[0]; //Acts like a leg piece, but its pitch is set to the minimum of the two legs

	public ModelCustomArmour() {
		super(new ArmorRenderConfig(), new BaseType());
	}

	@Override
	public void renderRightArm(AbstractClientPlayer clientPlayer, ModelBiped baseBiped) {
		ModelPlayer modelPlayer = getBonesFor(clientPlayer);
		modelPlayer.bipedRightArm.isHidden = false;
		modelPlayer.bipedRightArm.showModel = true;
		modelPlayer.swingProgress = 0.0F;
		modelPlayer.isSneak = false;
		modelPlayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
		modelPlayer.bipedRightArm.rotateAngleX = baseBiped.bipedRightArm.rotateAngleX;
		modelPlayer.bipedRightArm.rotateAngleY = baseBiped.bipedRightArm.rotateAngleY;
		modelPlayer.bipedRightArm.rotateAngleZ = baseBiped.bipedRightArm.rotateAngleZ;
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		render(rightArmModel, modelPlayer.bipedRightArm, 0.0625F, type.modelScale);
		GlStateManager.disableBlend();
	}

	@Override
	public void renderLeftArm(AbstractClientPlayer clientPlayer, ModelBiped baseBiped) {
		ModelPlayer modelPlayer = getBonesFor(clientPlayer);
		modelPlayer.bipedLeftArm.isHidden = false;
		modelPlayer.bipedLeftArm.showModel = true;
		modelPlayer.isSneak = false;
		modelPlayer.swingProgress = 0.0F;
		modelPlayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
		modelPlayer.bipedLeftArm.rotateAngleX = baseBiped.bipedLeftArm.rotateAngleX;
		modelPlayer.bipedLeftArm.rotateAngleY = baseBiped.bipedLeftArm.rotateAngleY;
		modelPlayer.bipedLeftArm.rotateAngleZ = baseBiped.bipedLeftArm.rotateAngleZ;
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		render(leftArmModel, modelPlayer.bipedLeftArm, 0.0625F, type.modelScale);
		GlStateManager.disableBlend();
	}

	private Bones getBonesFor(Entity entity) {
		Bones modelPlayer = entity instanceof AbstractClientPlayer && ((AbstractClientPlayer) entity).getSkinType().equals("slim") ?
			bonesSmall : bones;
		modelPlayer.armor = this;
		return modelPlayer;
		
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		GlStateManager.pushMatrix();
		GlStateManager.scale(type.modelScale, type.modelScale, type.modelScale);
		isSneak = entity.isSneaking();
		ItemStack itemstack = ((EntityLivingBase)entity).getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		rightArmPose = itemstack.isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;

		if(!itemstack.isEmpty())
		{
			EnumAction enumaction = itemstack.getItemUseAction();
			if(enumaction == EnumAction.BLOCK)
			{
				rightArmPose = ArmPose.BLOCK;
			}
			else if(enumaction == EnumAction.BOW)
			{
				rightArmPose = ArmPose.BOW_AND_ARROW;
			}
		}
		if(isSneak)
		{
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}
		
		Bones bones = getBonesFor(entity);
		
		render(headModel, bones.bipedHead, f5, type.modelScale);
		render(bodyModel, bones.bipedBody, f5, type.modelScale);
		render(leftArmModel, bones.bipedLeftArm, f5, type.modelScale);
		render(rightArmModel, bones.bipedRightArm, f5, type.modelScale);
		render(leftLegModel, bones.bipedLeftLeg, f5, type.modelScale);
		render(rightLegModel, bones.bipedRightLeg, f5, type.modelScale);
		//Skirt front
		{
			for(ModelRendererTurbo mod : skirtFrontModel)
			{
				mod.rotationPointX = (bones.bipedLeftLeg.rotationPointX + bones.bipedRightLeg.rotationPointX) / 2F / type.modelScale;
				mod.rotationPointY = (bones.bipedLeftLeg.rotationPointY + bones.bipedRightLeg.rotationPointY) / 2F / type.modelScale;
				mod.rotationPointZ = (bones.bipedLeftLeg.rotationPointZ + bones.bipedRightLeg.rotationPointZ) / 2F / type.modelScale;
				mod.rotateAngleX = Math.min(bones.bipedLeftLeg.rotateAngleX, bones.bipedRightLeg.rotateAngleX);
				mod.rotateAngleY = bones.bipedLeftLeg.rotateAngleY;
				mod.rotateAngleZ = bones.bipedLeftLeg.rotateAngleZ;
				mod.render(f5);
			}
		}
		//Skirt back
		{
			for(ModelRendererTurbo mod : skirtRearModel)
			{
				mod.rotationPointX = (bipedLeftLeg.rotationPointX + bipedRightLeg.rotationPointX) / 2F / type.modelScale;
				mod.rotationPointY = (bipedLeftLeg.rotationPointY + bipedRightLeg.rotationPointY) / 2F / type.modelScale;
				mod.rotationPointZ = (bipedLeftLeg.rotationPointZ + bipedRightLeg.rotationPointZ) / 2F / type.modelScale;
				mod.rotateAngleX = Math.max(bipedLeftLeg.rotateAngleX, bipedRightLeg.rotateAngleX);
				mod.rotateAngleY = bipedLeftLeg.rotateAngleY;
				mod.rotateAngleZ = bipedLeftLeg.rotateAngleZ;
				mod.render(f5);
			}
		}
		GlStateManager.popMatrix();
	}
	
	@Override
	public void render(String modelPart, ModelRenderer bodyPart, float f5, float scale) {
	}

	public void render(ModelRendererTurbo[] models, ModelRenderer bodyPart, float f5, float scale)
	{
		setToBodyPartRotationPoint(models, bodyPart, scale);
		setToBodyPartRotationAngles(models, bodyPart);
		render(models, f5);
	}
	
	public void render(ModelRendererTurbo[] models, float f5) {
		for(ModelRendererTurbo mod : models)
			mod.render(f5);
	}

	public void setToBodyPartRotationPoint(ModelRendererTurbo[] models, ModelRenderer bodyPart, float scale)
	{
		float yOff;
		if (bodyPart == bipedLeftArm || bodyPart == bipedRightArm)
			yOff = 0.5f;//0.25f;
		else yOff = 0f;

		for(ModelRendererTurbo mod : models)
		{
			mod.rotationPointX = bodyPart.rotationPointX / scale;
			mod.rotationPointY = bodyPart.rotationPointY / scale + yOff;
			mod.rotationPointZ = bodyPart.rotationPointZ / scale;
		}
	}
	
	public void setToBodyPartRotationAngles(ModelRendererTurbo[] models, ModelRenderer bodyPart) {
		for(ModelRendererTurbo mod : models)
		{
			mod.rotateAngleX = bodyPart.rotateAngleX;
			mod.rotateAngleY = bodyPart.rotateAngleY;
			mod.rotateAngleZ = bodyPart.rotateAngleZ;
		}
	}
}
