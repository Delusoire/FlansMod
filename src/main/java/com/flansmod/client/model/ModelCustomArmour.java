package com.flansmod.client.model;

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
import mchhui.modularmovements.tactical.client.ClientLitener;

import com.flansmod.client.tmt.ModelRendererTurbo;
import com.flansmod.common.teams.ArmourType;

public class ModelCustomArmour extends ModelBiped
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

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		// TODO Auto-generated method stub
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		ClientLitener.setRotationAngles(this, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
	}
 
 
 	public void renderRightArm(AbstractClientPlayer clientPlayer, ModelBiped baseBiped) {
		Bones bones = this.bones;
		if (clientPlayer.getSkinType().equals("slim")) {
			bones = this.bonesSmall;
		}
		bones.armor = this;
		float f = 1.0F;
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		float f1 = 0.0625F;
		ModelPlayer modelplayer = bones;
		modelplayer.bipedRightArm.isHidden = false;
		modelplayer.bipedRightArm.showModel = true;
		GlStateManager.enableBlend();
		modelplayer.swingProgress = 0.0F;
		modelplayer.isSneak = false;
		modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
		modelplayer.bipedRightArm.rotateAngleX = baseBiped.bipedRightArm.rotateAngleX;
		modelplayer.bipedRightArm.rotateAngleY = baseBiped.bipedRightArm.rotateAngleY;
		modelplayer.bipedRightArm.rotateAngleZ = baseBiped.bipedRightArm.rotateAngleZ;
		modelplayer.bipedRightArm.render(0.0625F);
		GlStateManager.disableBlend();
	}

	public void renderLeftArm(AbstractClientPlayer clientPlayer, ModelBiped baseBiped) {
		Bones bones = this.bones;
		if (clientPlayer.getSkinType().equals("slim")) {
			bones = this.bonesSmall;
		}
		bones.armor = this;
		float f = 1.0F;
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		float f1 = 0.0625F;
		ModelPlayer modelplayer = bones;
		modelplayer.bipedLeftArm.isHidden = false;
		modelplayer.bipedLeftArm.showModel = true;
		GlStateManager.enableBlend();
		modelplayer.isSneak = false;
		modelplayer.swingProgress = 0.0F;
		modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
		modelplayer.bipedLeftArm.rotateAngleX = baseBiped.bipedLeftArm.rotateAngleX;
		modelplayer.bipedLeftArm.rotateAngleY = baseBiped.bipedLeftArm.rotateAngleY;
		modelplayer.bipedLeftArm.rotateAngleZ = baseBiped.bipedLeftArm.rotateAngleZ;
		modelplayer.bipedLeftArm.render(0.0625F);
		GlStateManager.disableBlend();
    	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
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
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		if(isSneak)
		{
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}
		render(headModel, bipedHead, f5, type.modelScale);
		render(bodyModel, bipedBody, f5, type.modelScale);
		render(leftArmModel, bipedLeftArm, f5, type.modelScale);
		render(rightArmModel, bipedRightArm, f5, type.modelScale);
		render(leftLegModel, bipedLeftLeg, f5, type.modelScale);
		render(rightLegModel, bipedRightLeg, f5, type.modelScale);
		//Skirt front
		{
			for(ModelRendererTurbo mod : skirtFrontModel)
			{
				mod.rotationPointX = (bipedLeftLeg.rotationPointX + bipedRightLeg.rotationPointX) / 2F / type.modelScale;
				mod.rotationPointY = (bipedLeftLeg.rotationPointY + bipedRightLeg.rotationPointY) / 2F / type.modelScale;
				mod.rotationPointZ = (bipedLeftLeg.rotationPointZ + bipedRightLeg.rotationPointZ) / 2F / type.modelScale;
				mod.rotateAngleX = Math.min(bipedLeftLeg.rotateAngleX, bipedRightLeg.rotateAngleX);
				mod.rotateAngleY = bipedLeftLeg.rotateAngleY;
				mod.rotateAngleZ = bipedLeftLeg.rotateAngleZ;
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
	
	public void render(ModelRendererTurbo[] models, ModelRenderer bodyPart, float f5, float scale)
	{
		setBodyPart(models, bodyPart, scale);
		for(ModelRendererTurbo mod : models)
		{
			mod.rotateAngleX = bodyPart.rotateAngleX;
			mod.rotateAngleY = bodyPart.rotateAngleY;
			mod.rotateAngleZ = bodyPart.rotateAngleZ;
			mod.render(f5);
		}
	}
	
	public void setBodyPart(ModelRendererTurbo[] models, ModelRenderer bodyPart, float scale)
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
}
