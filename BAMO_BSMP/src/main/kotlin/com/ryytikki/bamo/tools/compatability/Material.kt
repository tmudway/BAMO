package com.ryytikki.bamo.tools.compatability

import net.minecraft.block.MapColor
import net.minecraft.block.piston.PistonBehavior

open class Material(blocksLight: Boolean, blocksMove: Boolean, col: MapColor, piston: PistonBehavior, burn: Boolean, liquid: Boolean, replace: Boolean, solid: Boolean) {

    val blocksLight:Boolean = blocksLight;
    val blocksMovement:Boolean = blocksMove;
    val color:MapColor = col;
    val pistonBehavior:PistonBehavior = piston;
    val burnable: Boolean = burn;
    val liquid: Boolean = liquid;
    val replaceable: Boolean = replace;
    val solid: Boolean = solid;

    object AIR:Material(false, false, MapColor.CLEAR, PistonBehavior.DESTROY, false, false, true, false)
    object BAMBOO:Material(false, false, MapColor.get(52), PistonBehavior.DESTROY, true, false, false, true)
    object BAMBOO_SAPLING:Material(false, false, MapColor.get(52), PistonBehavior.DESTROY, true, false, false, true)
    object BARRIER:Material(false, true, MapColor.CLEAR, PistonBehavior.BLOCK, false, false, false, true)
    object BUBBLE_COLUMN:Material(false, false, MapColor.get(48), PistonBehavior.DESTROY, false, true, true, false)
    object GLASS:Material(false, true, MapColor.CLEAR, PistonBehavior.NORMAL, false, false, false, true)
    object CACTUS:Material(false, false, MapColor.get(28), PistonBehavior.DESTROY, false, false, false, true)
    object CAKE:Material(true, true, MapColor.CLEAR, PistonBehavior.DESTROY, false, false, false, true)
    object ORGANIC_PRODUCT:Material(true, true, MapColor.DARK_GREEN, PistonBehavior.DESTROY, false, false, false, true)
    object UNDERWATER_PLANT:Material(true, false, MapColor.WATER_BLUE, PistonBehavior.DESTROY, false, false, false, false)
    object DECORATION:Material(false, false, MapColor.CLEAR, PistonBehavior.DESTROY, true, false, false, false)
    object SOIL:Material(true, true, MapColor.DIRT_BROWN, PistonBehavior.NORMAL, false, false, false, true)
    object EGG:Material(true, true, MapColor.DARK_GREEN, PistonBehavior.DESTROY, false, false, false, true)
    object TNT:Material(true, true, MapColor.RED, PistonBehavior.NORMAL, true, false, false, true)
    object FIRE:Material(false, false, MapColor.CLEAR, PistonBehavior.DESTROY, false, false, true, false)
    object SOLID_ORGANIC:Material(true, true, MapColor.DIRT_BROWN, PistonBehavior.NORMAL, false, false, false, true)
    object METAL:Material(true, true, MapColor.DIRT_BROWN, PistonBehavior.NORMAL, false, false, false, true)
    object ICE:Material(false, true, MapColor.LIGHT_BLUE_GRAY, PistonBehavior.NORMAL, false, false, false, true)
    object DENSE_ICE:Material(true, true, MapColor.LIGHT_BLUE_GRAY, PistonBehavior.NORMAL, false, false, false, true)
    object LAVA:Material(false, false, MapColor.RED, PistonBehavior.DESTROY, false, true, true, false)
    object LEAVES:Material(true, true, MapColor.DARK_GREEN, PistonBehavior.DESTROY, true, false, false, true)
    object NETHER_WOOD:Material(true, true, MapColor.BROWN, PistonBehavior.NORMAL, false, false, false, true)
    object PISTON:Material(true, true, MapColor.STONE_GRAY, PistonBehavior.BLOCK, false, false, false, true)
    object PLANT:Material(false, false, MapColor.GREEN, PistonBehavior.DESTROY, false, false, false, false)
    object PORTAL:Material(false, false, MapColor.CLEAR, PistonBehavior.BLOCK, false, false, false, false)
    object REPLACEABLE_PLANT:Material(false, false, MapColor.GREEN, PistonBehavior.DESTROY, true, false, true, false)
    object REPLACEABLE_FIREPROOF_PLANT:Material(false, false, MapColor.GREEN, PistonBehavior.DESTROY, false, false, true, false)
    object REPLACEABLE_UNDERWATER_PLANT:Material(false, false, MapColor.WATER_BLUE, PistonBehavior.DESTROY, false, false, true, false)
    object AGGREGATE:Material(true, true, MapColor.get(8), PistonBehavior.NORMAL, false, false, false, true)
    object SHULKER_BOX:Material(true, true, MapColor.PURPLE, PistonBehavior.NORMAL, false, false, false, true)
    object SNOW_BLOCK:Material(true, true, MapColor.WHITE, PistonBehavior.NORMAL, false, false, false, true)
    object SPONGE:Material(true, true, MapColor.YELLOW, PistonBehavior.NORMAL, false, false, false, true)
    object STONE:Material(true, true, MapColor.STONE_GRAY, PistonBehavior.NORMAL, false, false, false, true)
    object STRUCTURE_VOID:Material(false, false, MapColor.CLEAR, PistonBehavior.DESTROY, false, false, true, false)
    object SNOW_LAYER:Material(false, false, MapColor.WHITE, PistonBehavior.DESTROY, false, false, true, false)
    object GOURD:Material(true, true, MapColor.DARK_GREEN, PistonBehavior.DESTROY, false, false, false, true)
    object WATER:Material(true, false, MapColor.WATER_BLUE, PistonBehavior.DESTROY, false, true, true, false)
    object COBWEB:Material(true, false, MapColor.LIGHT_GRAY, PistonBehavior.DESTROY, false, false, false, true)
    object WOOD:Material(true, false, MapColor.BROWN, PistonBehavior.NORMAL, true, false, false, true)
    object WOOL:Material(true, false, MapColor.LIGHT_GRAY, PistonBehavior.NORMAL, true, false, false, true)


}