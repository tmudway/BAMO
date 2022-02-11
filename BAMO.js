(function () {
    var button;
    var exportWindow;
    var fileName;
    
    rotationTypes = [
        "default",
        "axis",
        "y_axis_player",
        "y_axis",
        "all_player",
        "all",
    ];

    soundOptions = [
        "Grass",
        "Glass",
        "Gravel",
        "Honey",
        "Metal",
        "Sand",
        "Snow",
        "Stone",
        "Wool",
        "Wood",
        "Ancient Debris",
        "Anvil",
        "Bamboo",
        "Bamboo Sapling",
        "Basalt",
        "Bone Brick",
        "Chain",
        "Coral Block",
        "Crop",
        "Fungus",
        "Gilded Blackstone",
        "Hard Crop",
        "Ladder",
        "Lantern",
        "Lily Pad",
        "Lodestone",
        "Netherite Block",
        "Netherrack",
        "Nether Bricks",
        "Nether Gold Ore",
        "Nether Ore",
        "Nether Sprouts",
        "Nether Wart",
        "Nylium",
        "Roots",
        "Scaffolding",
        "Shroomlight",
        "Slime Block",
        "Soul Sand",
        "Stem",
        "Sweet Berry Bush",
        "Twisting Vines",
        "Vine",
        "Wart Block",
        "Weeping Vines",
        "Wet Grass"
    ];

    materialOptions = [
        "Dirt",        
        "Air",
        "Bamboo",
        "Bamboo Sapling",
        "Barrier",
        "Bubble Column",
        "Buildable Glass",
        "Cactus",
        "Cake",
        "Clay",
        "Coral",
        "Cloth Decoration",
        "Decoration",
        "Egg",
        "Explosive",
        "Fire",
        "Glass",
        "Grass",
        "Heavy Metal",
        "Ice",
        "Ice Solid",
        "Lava",
        "Leaves",
        "Metal",
        "Nether Wood",
        "Piston",
        "Plant",
        "Portal",
        "Replaceable Plant",
        "Replaceable Fireproof Plant",
        "Replaceable Water Plant",
        "Sand",
        "Shulker Shell",
        "Snow",
        "Sponge",
        "Stone",
        "Structural Air",
        "Top Snow",
        "Vegetable",
        "Water",
        "Water Plant",
        "Web",
        "Wood",
        "Wool"
    ];

    tabOptions = [
        "Building Blocks",
        "Brewing",
        "Combat",
        "Food",
        "Decorations",
        "Misc",
        "Redstone",
        "Tools",
        "Transportation"
    ];

    vueComponent = {
        data: function(){
            return{
                namespace: "bamo",
                blockName: "",

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
                creativeTab: tabOptions[0],
                advModeDisplay: false
            }
        },
        template: `
            <div>
                <div id="MainWindow">
                    <table>
                        <tr>
                            <td>Display Name: </td>
                            <td><input type="text" class="dark_bordered" v-model="displayName"></td>
                        </tr>
                        <tr>
                            <td>Material: </td>
                            <td><select v-model="material"><option v-for="op in materialOptions" v-bind:value="op">{{ op }}</option></select></td>
                        </tr>
                        <tr>
                            <td>Blast Resistance: </td>
                            <td><input type="number" class="dark_bordered" v-model.number="blastRes" min=0></td>
                        </tr>
                        <tr>
                            <td>Slipperiness: </td>
                            <td><input type="number" class="dark_bordered" v-model.number="slip" min=0></td>
                        </tr>
                        <tr>
                            <td>Gravity: </td>
                            <td><input type="checkbox" v-model="gravity"></td>
                        </tr>
                        <tr>
                            <td>Rotation Type: </td>
                            <td><select id="RotationType" v-model="rotType"><option v-for="op in rotationTypes" v-bind:value="op">{{ op }}</option></select></td>
                        </tr>
                        <tr>
                            <td>Block Sounds: </td>
                            <td><select v-model="sounds"><option v-for="op in soundOptions" v-bind:value="op">{{ op }}</option></select></td>
                        </tr>
                        <tr>
                            <td>Luminance: </td>
                            <td><input type="number" class="dark_bordered" v-model.number="lum" min=0></td>
                        </tr>
                        <tr>
                            <td>Max Stack Size: </td>
                            <td><input type="number" class="dark_bordered" v-model.number="maxStack" min=0></td>
                        </tr>
                        <tr>
                            <td>Fireproof: </td>
                            <td><input type="checkbox" v-model="fireproof"></td>
                        </tr>
                        <tr>
                            <td>Creative Tab: </td>
                            <td><select v-model="creativeTab"><option v-for="op in tabOptions" v-bind:value="op">{{ op }}</option></select></td>
                        </tr>
                    </table>
                </div>
            </div>
        `,
        methods: {
            changeDisplayName: function(str){
                this.displayName = str;
                return;
            },

            createJSON: function(event){

                // Define folder locations
                var dataFolder = settings.minecraftFolder.value + "\\bamo\\data\\"
                var blockstatesFolder = settings.minecraftFolder.value + "\\bamo\\assets\\" + this.namespace + "\\blockstates\\"
                var blockModelsFolder = settings.minecraftFolder.value + "\\bamo\\assets\\" + this.namespace + "\\models\\block\\"
                var itemModelsFolder = settings.minecraftFolder.value + "\\bamo\\assets\\" + this.namespace + "\\models\\item\\"
                var blockTexturesFolder = settings.minecraftFolder.value + "\\bamo\\assets\\" + this.namespace + "\\textures\\blocks\\"

                // Create the folders if they dont exist
                var folderList = [dataFolder, blockstatesFolder, blockModelsFolder, itemModelsFolder, blockTexturesFolder]
                var fs = require('fs');

                folderList.forEach(function(folder){
                    if (!fs.existsSync(folder)){
                        fs.mkdirSync(folder, {recursive: true});
                    }
                })

                // Create mcmeta file if it doesnt exist
                if (!fs.existsSync(settings.minecraftFolder.value + "\\bamo\\pack.mcmeta")){
                    mcmetaData = {"pack" : {"pack_format" : 6, "description" : "Resource Pack for BAMO test files"}};
                    fs.writeFile(settings.minecraftFolder.value + "\\bamo\\pack.mcmeta", JSON.stringify(mcmetaData), "utf8", (err) => {console.log("Error generating mcmeta file:", err);});
                }

                // Generate block name from the displayname
                var blockName = this.displayName.replace(/\s+/g, '').toLowerCase();

                // Create blockstates files
                var stateData = {"variants" : {"" : {"model" : this.namespace + ":block/" + blockName}}};
                fs.writeFile(blockstatesFolder + "\\" + blockName + ".json", JSON.stringify(stateData), "utf8", (err) => {console.log("Error generating blockstate:", err);});

                // Pull the model data from the codec
                var modelData = JSON.parse(Format.codec.compile())

                // Add the parent data
                modelData["parent"] = "block/block"

                // Add namespace to textures if needed
                Object.keys(modelData.textures).forEach((key) => {
                    if (modelData.textures[key].includes(":") == false){
                        modelData.textures[key] = this.namespace + ":blocks/" + modelData.textures[key]
                    }
                })

                // Write model files
                fs.writeFile(blockModelsFolder + "\\" + blockName + ".json", JSON.stringify(modelData), "utf8", err => {console.log("Error Found writing block model data:", err);});
                fs.writeFile(itemModelsFolder + "\\" + blockName + ".json", JSON.stringify(modelData), "utf8", err => {console.log("Error Found writing item model data:", err);});

                // Copy texture files
                Texture.all.forEach(function(tx){
                    var image = nativeImage.createFromPath(tx.source.replace(/\?\d+$/, '')).toPNG()
                    fs.writeFile(blockTexturesFolder + "\\" + tx.name, image, (err) => {console.log("Error Found writing texture data:", err);});
                })
                
                // Write block properties file

                var data = {
                    "displayName" : this.displayName,
                    "material" : this.material,
                    "blastRes" : this.blastRes,
                    "slip" : this.slip,
                    "gravity" : this.gravity,
                    "rotType" : this.rotType,
                    "sounds" : this.sounds,
                    "lum" : this.lum,
                    "maxStack" : this.maxStack,
                    "fireproof" : this.fireproof,
                    "creativeTab" : this.creativeTab,
                };

                fs.writeFile(dataFolder + "\\" + blockName + ".json", JSON.stringify(data), "utf8", err => {console.log("Error writing block properties:", err);});
            }
        }
    }
    
    Plugin.register('BAMO', {
        title: 'BAMO Exporter',
        author: 'Ryytikki',
        description: 'Exports block metadata for use in the BAMO mod',
        icon: 'bar_chart',
        version: '0.0.1',
        variant: 'both',
        
        onload() {

            fileName = "";

            // Setting that holds the resource pack folder location
            minecraftFolder = new Setting('minecraftFolder', {
                name : 'Resource Pack Folder Location',
                description: 'Location of the resource pack folder on your PC',
                category: 'export',
                type: 'text',
                value: ''
            })
            
            // Export button in menu
            button = new Action('block_mod', {
                name: 'BAMO',
                description: 'Exports block metadata for BAMO mod',
                icon: 'fa-cube',
                click: function () {
                    if (Project.name != undefined){
                        if ((Settings.get('minecraftFolder') != undefined) && (Settings.get('minecraftFolder') != '')){
                            exportWindow.show()
                        }else{
                            Blockbench.showMessageBox({buttons: ["Ok"], title: "Error", message: "You must set your Resources folder location under Settings->Export"});
                        }
                    }else{
                        Blockbench.showMessageBox({buttons: ["Ok"], title: "Error", message: "Please ensure your file is saved before exporting. If you see this for a saved file, reload it and try again"})
                    }
                }
            });

            MenuBar.addAction(button, 'file');

            // Dialog that opens when you click the button1
            // See the vueComponent object for details
            exportWindow = new Dialog('C&CExportWindow', {
                title: 'BAMO Exporter',
                component: vueComponent,

                onConfirm: function(){
                    exportWindow.content_vue.createJSON();
                }
            })

            // Pull the filename when loading/saving to use for file dialog
            Blockbench.on("load_project", function() {
                fileName = Project.name;
                exportWindow.content_vue.changeDisplayName(Project.name);
            })

            Blockbench.on("save_project", function() {
                if (fileName != Project.name){
                    fileName = Project.name;
                    exportWindow.content_vue.changeDisplayName(fileName);
                }
               // exportWindow.content_vue.displayName = fileName;
            })
        },
        
        onunload() {
            button.delete();
            minecraftFolder.delete();
        }
    });

})();