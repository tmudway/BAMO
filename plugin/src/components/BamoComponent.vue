<script>
import { rotationTypes, soundOptions, materialOptions, transparencyOptions, tabOptions, customTypeOptions } from '../util/OptionArrays.js';
import {genWallState, genStairState} from '../util/GenStates.js'
import {genLootTable, genMineableTag} from '../util/GenDataFiles.js'
import {dictFromTexture, cleanFileName} from '../util/Utils.js'

export default {
    data() {
        return {
            step: "start",
            error: false,

            rotationTypes: rotationTypes,
            soundOptions: soundOptions,
            materialOptions: materialOptions,
            transparencyOptions: transparencyOptions,
            tabOptions: tabOptions,
            customTypeOptions:customTypeOptions,

            // Data to be exported
            properties: {
                namespace: "bamo",
                displayName: "test",
                material: materialOptions[0],
                blastRes: 6,
                slip: 0.6,
                gravity: false,
                rotType: rotationTypes[0],
                sounds: soundOptions[0],
                lum: 0,
                maxStack: 64,
                fireproof: true,
                transparency: transparencyOptions[0],
                creativeTab: tabOptions[0],
            },

            custom: {
                bounds: [],
            },

            variant: {
                default:{
                    all: Texture.all[0].name,
                },
                stair: {
                    top: Texture.all[0].name,
                    bottom: Texture.all[0].name,
                    side: Texture.all[0].name,
                    particle: Texture.all[0].name
                },
                slab: {
                    top: Texture.all[0].name,
                    bottom: Texture.all[0].name,
                    side: Texture.all[0].name,
                    particle: Texture.all[0].name
                },
                wall: {
                    wall:Texture.all[0].name,
                    particle: Texture.all[0].name
                }
            },

            types: {
                custom: true,
                customType: "Default",
                block: false,
                stair: false,
                slab: false,
                wall: false,
            }

        }
    },
    computed:{
        
    },
    methods:{

        Textures(){
            return Texture.all;
        },

        changePage(event, page){
            if (this.properties.displayName == ""){
                this.error = true;
            }else{

                if ((this.step == "types" && page == "physical") || (this.step == "physical" && page == "types")){
                    if (this.types.block) {page = "variant"}
                }

                this.error = false;
                this.step = page;
            }
        },

        toggleType(event, type){
            
            if (this.types.custom && type=="custom"){
                this.types.block = false
                this.types.stair = false
                this.types.slab = false
                this.types.wall = false
                return
            }

            if (this.types.block && type=="block"){
                this.types.custom = false
                return
            }
        },

        reset(event){
            this.error = false;
            this.step = "start";
        },

        createJSON(event){

            if (this.properties.displayName == ""){
                this.error = true;
                return;
            }

            var JSZip = require("jszip");
            var zip = new JSZip();

            var packName = cleanFileName(this.properties.displayName);

            // Define folder locations
            var objFolder = settings.minecraftFolder.value + "\\bamopacks\\" + packName + "\\objects\\";
            var blockstatesFolder = settings.minecraftFolder.value + "\\bamopacks\\" + packName + "\\assets\\" + this.properties.namespace + "\\blockstates\\";
            var blockModelsFolder = settings.minecraftFolder.value + "\\bamopacks\\" + packName + "\\assets\\" + this.properties.namespace + "\\models\\block\\";
            var itemModelsFolder = settings.minecraftFolder.value + "\\bamopacks\\" + packName + "\\assets\\" + this.properties.namespace + "\\models\\item\\";
            var blockTexturesFolder = settings.minecraftFolder.value + "\\bamopacks\\" + packName + "\\assets\\" + this.properties.namespace + "\\textures\\blocks\\";
            var dataFolder = settings.minecraftFolder.value + "\\bamopacks\\" + packName + "\\data\\";

            // Create the folders if they dont exist
            var folderList = [objFolder, blockstatesFolder, blockModelsFolder, itemModelsFolder, blockTexturesFolder, dataFolder];
            var fs = require('fs');

            folderList.forEach(function(folder){
                if (!fs.existsSync(folder)){
                    fs.mkdirSync(folder, {recursive: true});
                }
            })

            // Create mcmeta file
            var mcmetaData = {"pack" : {"pack_format" : 6, "description" : "Resource Pack for BAMO test files"}};
            fs.writeFile(settings.minecraftFolder.value + "\\bamopacks\\" + packName + "\\pack.mcmeta", JSON.stringify(mcmetaData), "utf8", (err) => {if (err != null) {console.log("Error generating mcmeta file:", err);}});
            zip.file("pack.mcmeta", JSON.stringify(mcmetaData))

            // Generate block name from the displayname
            var blockName = cleanFileName(this.properties.displayName);

            //generate the list of blocks to be exported
            var blockList = [];

            var codecData = Format.codec.compile();

            // Custom Block
            if (this.types.custom){
                // Pull the model data from the codec
                var modelData = JSON.parse(codecData);
                modelData["parent"] = "block/block";

                var stateData = "";
                // Create blockstates data
                if (this.properties.rotType == "y_axis"){
                    stateData = JSON.stringify({"variants" : {"facing=north" : {"model" : this.properties.namespace + ":block/" + blockName},
                                                                "facing=east" : {"model" : this.properties.namespace + ":block/" + blockName, "y": 90},
                                                                "facing=south" : {"model" : this.properties.namespace + ":block/" + blockName, "y": 180},
                                                                "facing=west" : {"model" : this.properties.namespace + ":block/" + blockName, "y": 270},
                                                                }})
                }else{
                    stateData = JSON.stringify({"variants" : {"" : {"model" : this.properties.namespace + ":block/" + blockName}}});
                }
                
                var textureData = {}

                // Setup texture dict
                ns = this.properties.namespace
                Object.keys(modelData.textures).forEach((key) => {
                    Texture.all.forEach(function(tx){
                        if ((tx.id == key) || (key == "particle" && tx.particle == true)){
                            if (tx.namespace == ""){
                                textureData[key] = ns + ":blocks/" + cleanFileName(tx.name.split(".")[0]);
                            }else{
                                textureData[key] = tx.namespace + ":" + tx.folder + "/" + cleanFileName(tx.name.split(".")[0])
                            } 
                        }
                    })
                })

                modelData.textures = textureData

                var boxList = [];
                modelData.elements.forEach((model) =>{
                    boxList.push([model["from"], model["to"]]);
                });

                blockList.push({"name": blockName, "types": [], "model": modelData, "state": stateData, "hitbox": boxList});
            }

            // Regular Block
            if (this.types.block){
                var modelData = {};
                modelData["credit"] = codecData["credit"];
                modelData["parent"] = "block/cube_all";
                modelData["textures"] = {
                    "all": dictFromTexture(this.variant.default.all, this.properties.namespace),
                    "particle": dictFromTexture("particle", this.properties.namespace)
                };

                var state = JSON.stringify({"variants": {"": {"model": this.properties.namespace + ":block/" + blockName}}});

                var typeList = [];
                if (this.types.stair) typeList.push("stairs");
                if (this.types.slab) typeList.push("slab");
                if (this.types.wall) typeList.push("wall");

                blockList.push({"name": blockName, "types": typeList, "state": state, "model": modelData, "hitbox": []});
            }

            // Stair Block
            if (this.types.stair){

                var name = blockName + "_stairs";

                var modelData = {};
                modelData["credit"] = codecData["credit"];
                modelData["parent"] = "minecraft:block/stairs";
                modelData["textures"] = {
                    "top": dictFromTexture(this.variant.stair.top, this.properties.namespace),
                    "bottom": dictFromTexture(this.variant.stair.bottom, this.properties.namespace), 
                    "side": dictFromTexture(this.variant.stair.side, this.properties.namespace),
                    "particle": dictFromTexture("particle", this.properties.namespace)
                };
                
                // Write state
                var state = genStairState(this.properties.namespace, "block/" + name, "block/" + name + "_outer", "block/" + name + "_inner")
                fs.writeFile(blockstatesFolder + "\\" + name + ".json", state, "utf8", (err) => {if (err != null) {console.log("Error generating blockstate:", err);}});
                zip.file("assets/" + this.properties.namespace + "/blockstates/" + name + ".json", state)
                // write the 4 stair models
                // Base
                fs.writeFile(blockModelsFolder + "\\" + name + ".json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                zip.file("assets/" + this.properties.namespace + "/models/block/" + name + ".json", JSON.stringify(modelData))
                // Item
                fs.writeFile(itemModelsFolder + "\\" + name + ".json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing item model data:", err);}});
                zip.file("assets/" + this.properties.namespace + "/models/item/" + name + ".json", JSON.stringify(modelData))
                // Inner
                modelData["parent"] = "minecraft:block/inner_stairs"
                fs.writeFile(blockModelsFolder + "\\" + name + "_inner.json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                zip.file("assets/" + this.properties.namespace + "/models/block/" + name + "_inner.json", JSON.stringify(modelData))
                // Outer
                modelData["parent"] = "minecraft:block/outer_stairs"
                fs.writeFile(blockModelsFolder + "\\" + name + "_outer.json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                zip.file("assets/" + this.properties.namespace + "/models/block/" + name + "_outer.json", JSON.stringify(modelData))
            }

            if (this.types.slab){

                var name = blockName + "_slab";

                var modelData = {};
                modelData["credit"] = codecData["credit"];
                modelData["parent"] = "minecraft:block/slab"
                modelData["textures"] = {
                    "top": dictFromTexture(this.variant.slab.top, this.properties.namespace),
                    "bottom": dictFromTexture(this.variant.slab.bottom, this.properties.namespace), 
                    "side": dictFromTexture(this.variant.slab.side, this.properties.namespace),
                    "particle": dictFromTexture("particle", this.properties.namespace)
                }

                // Write State
                var state = {"variants" : {
                    "type=bottom" : {"model": this.properties.namespace + ":block/" + name}, 
                    "type=double":{"model":this.properties.namespace + ":block/" + blockName},
                    "type=top": {"model": this.properties.namespace + ":block/" + name + "_top"}
                }}
                fs.writeFile(blockstatesFolder + "\\" + name + ".json", JSON.stringify(state), "utf8", (err) => {if (err != null) {console.log("Error generating blockstate:", err);}});
                zip.file("assets/" + this.properties.namespace + "/blockstates/" + name + ".json", JSON.stringify(state))

                // Write the 3 slab models
                // Base
                fs.writeFile(blockModelsFolder + "\\" + name + ".json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                zip.file("assets/" + this.properties.namespace + "/models/block/" + name + ".json", JSON.stringify(modelData))
                // Item
                fs.writeFile(itemModelsFolder + "\\" + name + ".json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing item model data:", err);}});
                zip.file("assets/" + this.properties.namespace + "/models/item/" + name + ".json", JSON.stringify(modelData))
                // Top
                modelData["parent"] = "minecraft:block/slab_top"
                fs.writeFile(blockModelsFolder + "\\" + name + "_top.json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                zip.file("assets/" + this.properties.namespace + "/models/block/" + name + "_top.json", JSON.stringify(modelData))
            }

            if (this.types.wall){
                var name = blockName + "_wall";

                var modelData = {};
                modelData["credit"] = codecData["credit"];
                modelData["parent"] = "minecraft:block/template_wall_post";
                modelData["textures"] = {
                    "wall": dictFromTexture(this.variant.wall.wall, this.properties.namespace),
                    "particle": dictFromTexture("particle", this.properties.namespace)
                }

                // Write State
                var state = genWallState(this.properties.namespace, "block/" + name + "_post", "block/" + name + "_side", "block/" + name + "_side_tall")
                fs.writeFile(blockstatesFolder + "\\" + name + ".json", state, "utf8", (err) => {if (err != null) {console.log("Error generating blockstate:", err);}});
                zip.file("assets/" + this.properties.namespace + "/blockstates/" + name + ".json", state)

                // Write the 4  wall models
                // Base
                fs.writeFile(blockModelsFolder + "\\" + name + "_post.json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                zip.file("assets/" + this.properties.namespace + "/models/block/" + name + "_post.json", JSON.stringify(modelData))
                // Item
                modelData["parent"] = "minecraft:block/wall_inventory"
                fs.writeFile(itemModelsFolder + "\\" + name + ".json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing item model data:", err);}});
                zip.file("assets/" + this.properties.namespace + "/models/item/" + name + ".json", JSON.stringify(modelData))
                // Side
                modelData["parent"] = "minecraft:block/template_wall_side"
                fs.writeFile(blockModelsFolder + "\\" + name + "_side.json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                zip.file("assets/" + this.properties.namespace + "/models/block/" + name + "_side.json", JSON.stringify(modelData))
                // Side Tall
                modelData["parent"] = "minecraft:block/template_wall_side_tall"
                fs.writeFile(blockModelsFolder + "\\" + name + "_side_tall.json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing item model data:", err);}});
                zip.file("assets/" + this.properties.namespace + "/models/block/" + name + "_side_tall.json", JSON.stringify(modelData))

                // Deal with the tags
                var wallTags = dataFolder + "minecraft\\tags\\blocks\\walls.json"
                var tagVal = this.properties.namespace + ":" + name

                fs.mkdirSync(dataFolder + "minecraft\\tags\\blocks\\", {recursive: true});
                var tagData = {"replace" : false, "values": [tagVal]};

                fs.writeFile(wallTags, JSON.stringify(tagData), "utf8", err => {if (err != null) {console.log("Error found when writing wall tags:", err)}});
                zip.file("data/minecraft/tags/blocks/walls.json", JSON.stringify(tagData))
            }

            var modelData = JSON.parse(codecData);
            var ns = this.properties.namespace
            // Copy texture files
            Texture.all.forEach(function(tx){
                var image;
                if (tx.namespace != "minecraft"){
                    if (tx.img.currentSrc.slice(0, 4) == "data"){
                        image = nativeImage.createFromDataURL(tx.img.currentSrc).toPNG();
                    }else if(tx.img.currentSrc.slice(0, 4) == "file"){
                        image = nativeImage.createFromPath(tx.source.replace(/\?\d+$/, '')).toPNG();
                    }
        
                    fs.writeFile(blockTexturesFolder + "\\" + cleanFileName(tx.name), image, (err) => {if (err != null) {console.log("Error Found writing texture data:", err);}});
                    zip.file("assets/" + ns + "/textures/blocks/" + cleanFileName(tx.name), image)
                }
            })

            blockList.forEach(block => {

                // Generate data files

                // Looting file
                var lootData = genLootTable(this.properties.namespace, block["name"], block["types"])
                var lootTags = dataFolder + block["name"] + "\\loot_tables\\blocks\\" + block["name"] + ".json"

                fs.mkdirSync(dataFolder + block["name"] + "\\loot_tables\\blocks\\", {recursive: true});
                fs.writeFile(lootTags, lootData, "utf8", err => {if (err != null) {console.log("Error found when writing wall tags:", err)}});
                zip.file("data/" + this.properties.namespace + "/loot_tables/blocks/"+ block["name"] + ".json", lootData)

                // Mining file
                var mineableData = genMineableTag(this.properties.namespace, block["name"], block["types"])
                var mineableTags = dataFolder + "minecraft\\tags\\blocks\\mineable\\pickaxe.json"

                fs.mkdirSync(dataFolder + "minecraft\\tags\\blocks\\mineable", {recursive: true});
                fs.writeFile(mineableTags, mineableData, "utf8", err => {if (err != null) {console.log("Error found when writing wall tags:", err)}});
                zip.file("data/minecraft/tags/blocks/mineable/pickaxe.json", mineableData)

                // Write state file
                fs.writeFile(blockstatesFolder + "\\" + block["name"] + ".json", block["state"], "utf8", (err) => {if (err != null) {console.log("Error generating blockstate:", err);}});
                zip.file("assets/" + this.properties.namespace + "/blockstates/" + block["name"] + ".json", block["state"])

                // Write model files
                fs.writeFile(blockModelsFolder + "\\" + block["name"] + ".json", JSON.stringify(block["model"]), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                zip.file("assets/" + this.properties.namespace + "/models/block/" + block["name"] + ".json", JSON.stringify(block["model"]))
                fs.writeFile(itemModelsFolder + "\\" + block["name"] + ".json", JSON.stringify(block["model"]), "utf8", err => {if (err != null) {console.log("Error Found writing item model data:", err);}});
                zip.file("assets/" + this.properties.namespace + "/models/item/" + block["name"] + ".json", JSON.stringify(block["model"]))
                
                // Write block properties file
                var data = {
                    "displayName" : this.properties.displayName, 
                    "typeList" : block["types"],
                    "material" : this.properties.material,
                    "blastRes" : this.properties.blastRes,
                    "slip" : this.properties.slip,
                    "gravity" : this.properties.gravity,
                    "rotType" : this.properties.rotType,
                    "sounds" : this.properties.sounds,
                    "lum" : this.properties.lum,
                    "maxStack" : this.properties.maxStack,
                    "fireproof" : this.properties.fireproof,
                    "creativeTab" : this.properties.creativeTab,
                    "transparency": this.properties.transparency,
                    "hitbox": block["hitbox"],
                    "blockType" : this.types.customType,
                    "nameGenType" : "3.3" // Allows for names where " " is replaced with "_" to coexist with the older "" system
                };

                fs.writeFile(objFolder + "\\" + block["name"] + ".json", JSON.stringify(data), "utf8", err => {if (err != null) {console.log("Error writing block properties:", err);}});
                zip.file("objects/" + block["name"] + ".json", JSON.stringify(data))
            })

            zip.generateAsync({type:"nodebuffer"})
            .then(function (content) {
                fs.writeFile(settings.minecraftFolder.value + "\\bamopacks\\" + packName + ".zip", content, err => {});
            });

            fs.rm(settings.minecraftFolder.value + "\\bamopacks\\" + packName, {recursive: true}, err => {});

            let e = open_interface;
            e.hide();
        },
    }
}
</script>

<template src="./ComponentTemplate.html"></template>