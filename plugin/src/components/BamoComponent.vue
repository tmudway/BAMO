<script>
import { rotationTypes, soundOptions, materialOptions, transparencyOptions, tabOptions } from '../util/OptionArrays.js';
import {genWallState, genStairState} from '../util/GenStates.js'
import {imageNameToTexture} from '../util/Utils.js'

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
                stair: {
                    top: Texture.all[0].name,
                    bottom: Texture.all[0].name,
                    side: Texture.all[0].name,
                    particle: Texture.all[0].name
                },
                slab: {
                    top: Texture.all[0],
                    bottom: Texture.all[0],
                    side: Texture.all[0],
                    particle: Texture.all[0]
                },
                wall: {
                    wall:Texture.all[0],
                    particle: Texture.all[0]
                }
            },

            types: {
                custom: true,
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

            var packName = this.properties.displayName.replace(/\s+/g, '').toLowerCase();//"bamo";

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

            // Create mcmeta file if it doesnt exist
            if (!fs.existsSync(settings.minecraftFolder.value + "\\bamopacks\\" + packName + "\\pack.mcmeta")){
                var mcmetaData = {"pack" : {"pack_format" : 6, "description" : "Resource Pack for BAMO test files"}};
                fs.writeFile(settings.minecraftFolder.value + "\\bamopacks\\" + packName + "\\pack.mcmeta", JSON.stringify(mcmetaData), "utf8", (err) => {if (err != null) {console.log("Error generating mcmeta file:", err);}});
            }

            // Generate block name from the displayname
            var blockName = this.properties.displayName.replace(/\s+/g, '').toLowerCase();

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

                // Add namespace to textures if needed
                Object.keys(modelData.textures).forEach((key) => {

                    // Need to work out what format the texture is in
                    console.log(modelData.textures[key])
                    // Object
                    if (modelData.textures[key].constructor == Object){
                        textureData[key] = this.properties.namespace + ":blocks/" + modelData.textures[key]["name"].split(".")[0].replace(/\s+/g, '').toLowerCase()
                    
                    // String, but with multiple /
                    }else if (modelData.textures[key].match("[a-z_]+/[a-z_]+")){
                        textureData[key] = this.properties.namespace + ":blocks/" + modelData.textures[key].split("/")[1].replace(/\s+/g, '').toLowerCase();

                    // Other unformatted String
                    }else if (!modelData.textures[key].match("bamo:blocks/[a-z_]+")){
                        textureData[key] = this.properties.namespace + ":blocks/" + modelData.textures[key].replace(/\s+/g, '').toLowerCase();

                    // Formatted String
                    }else{
                        textureData[key] = modelData.textures[key].replace(/\s+/g, '').toLowerCase()
                    }
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
                    "all": imageNameToTexture(this.properties.namespace, "blocks", Texture.all[0]),
                    "particle": imageNameToTexture(this.properties.namespace, "blocks", Texture.all[0])
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
                    "top": imageNameToTexture(this.properties.namespace, "blocks", this.variant.stair.top),
                    "bottom": imageNameToTexture(this.properties.namespace, "blocks", this.variant.stair.bottom),
                    "side": imageNameToTexture(this.properties.namespace, "blocks", this.variant.stair.side),
                    "particle": imageNameToTexture(this.properties.namespace, "blocks", this.variant.stair.top)
                };
                
                // Write state
                var state = genStairState(this.properties.namespace, "block/" + name, "block/" + name + "_outer", "block/" + name + "_inner")
                fs.writeFile(blockstatesFolder + "\\" + name + ".json", state, "utf8", (err) => {if (err != null) {console.log("Error generating blockstate:", err);}});

                // write the 4 stair models
                // Base
                fs.writeFile(blockModelsFolder + "\\" + name + ".json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                // Item
                fs.writeFile(itemModelsFolder + "\\" + name + ".json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing item model data:", err);}});
                // Inner
                modelData["parent"] = "minecraft:block/inner_stairs"
                fs.writeFile(blockModelsFolder + "\\" + name + "_inner.json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                // Outer
                modelData["parent"] = "minecraft:block/outer_stairs"
                fs.writeFile(blockModelsFolder + "\\" + name + "_outer.json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
            }

            if (this.types.slab){

                var name = blockName + "_slab";

                var modelData = {};
                modelData["credit"] = codecData["credit"];
                modelData["parent"] = "minecraft:block/slab"
                modelData["textures"] = {
                    "top": imageNameToTexture(this.properties.namespace, "blocks", this.variant.slab.top),
                    "bottom": imageNameToTexture(this.properties.namespace, "blocks", this.variant.slab.bottom),
                    "side": imageNameToTexture(this.properties.namespace, "blocks", this.variant.slab.side),
                    "particle": imageNameToTexture(this.properties.namespace, "blocks", this.variant.slab.top)
                }

                // Write State
                var state = {"variants" : {
                    "type=bottom" : {"model": this.properties.namespace + ":block/" + name}, 
                    "type=double":{"model":this.properties.namespace + ":block/" + blockName},
                    "type=top": {"model": this.properties.namespace + ":block/" + name + "_top"}
                }}
                fs.writeFile(blockstatesFolder + "\\" + name + ".json", JSON.stringify(state), "utf8", (err) => {if (err != null) {console.log("Error generating blockstate:", err);}});

                // Write the 3 slab models
                // Base
                fs.writeFile(blockModelsFolder + "\\" + name + ".json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                // Item
                fs.writeFile(itemModelsFolder + "\\" + name + ".json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing item model data:", err);}});
                // Top
                modelData["parent"] = "minecraft:block/slab_top"
                fs.writeFile(blockModelsFolder + "\\" + name + "_top.json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
            }

            if (this.types.wall){
                var name = blockName + "_wall";

                var modelData = {};
                modelData["credit"] = codecData["credit"];
                modelData["parent"] = "minecraft:block/template_wall_post";
                modelData["textures"] = {
                    "wall": imageNameToTexture(this.properties.namespace, "blocks", this.variant.wall.wall),
                    "particle": imageNameToTexture(this.properties.namespace, "blocks", this.variant.wall.wall)
                }

                // Write State
                var state = genWallState(this.properties.namespace, "block/" + name + "_post", "block/" + name + "_side", "block/" + name + "_side_tall")
                fs.writeFile(blockstatesFolder + "\\" + name + ".json", state, "utf8", (err) => {if (err != null) {console.log("Error generating blockstate:", err);}});
                
                // Write the 4  wall models
                // Base
                fs.writeFile(blockModelsFolder + "\\" + name + "_post.json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                // Item
                modelData["parent"] = "minecraft:block/wall_inventory"
                fs.writeFile(itemModelsFolder + "\\" + name + ".json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing item model data:", err);}});
                // Side
                modelData["parent"] = "minecraft:block/template_wall_side"
                fs.writeFile(blockModelsFolder + "\\" + name + "_side.json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                // Side Tall
                modelData["parent"] = "minecraft:block/template_wall_side_tall"
                fs.writeFile(blockModelsFolder + "\\" + name + "_side_tall.json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing item model data:", err);}});

                // Deal with the tags
                var wallTags = dataFolder + "minecraft\\tags\\blocks\\walls.json"
                var tagVal = this.properties.namespace + ":" + name

                fs.mkdirSync(dataFolder + "minecraft\\tags\\blocks\\", {recursive: true});
                tagData = {"replace" : false, "values": [tagVal]};

                fs.writeFile(wallTags, JSON.stringify(tagData), "utf8", err => {if (err != null) {console.log("Error found when writing wall tags:", err)}});
            }

            var modelData = JSON.parse(codecData);

            // Copy texture files
            Texture.all.forEach(function(tx){
                var image;
                if (tx.img.currentSrc.slice(0, 4) == "data"){
                    image = nativeImage.createFromDataURL(tx.img.currentSrc).toPNG();
                }else if(tx.img.currentSrc.slice(0, 4) == "file"){
                    image = nativeImage.createFromPath(tx.source.replace(/\?\d+$/, '')).toPNG();
                }
    
                fs.writeFile(blockTexturesFolder + "\\" + tx.name.replace(/\s+/g, '').toLowerCase(), image, (err) => {if (err != null) {console.log("Error Found writing texture data:", err);}});
            })

            blockList.forEach(block => {
                // Write state file
                fs.writeFile(blockstatesFolder + "\\" + block["name"] + ".json", block["state"], "utf8", (err) => {if (err != null) {console.log("Error generating blockstate:", err);}});

                // Write model files
                fs.writeFile(blockModelsFolder + "\\" + block["name"] + ".json", JSON.stringify(block["model"]), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                fs.writeFile(itemModelsFolder + "\\" + block["name"] + ".json", JSON.stringify(block["model"]), "utf8", err => {if (err != null) {console.log("Error Found writing item model data:", err);}});
                
                // Write block properties file
                var data = {
                    "displayName" : this.properties.displayName, //block["name"], 
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
                };

                fs.writeFile(objFolder + "\\" + block["name"] + ".json", JSON.stringify(data), "utf8", err => {if (err != null) {console.log("Error writing block properties:", err);}});
            })
            let e = open_interface;
            e.hide();
        },
    }
}
</script>

<template src="./ComponentTemplate.html"></template>