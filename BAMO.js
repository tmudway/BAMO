(function () {
    var btn;
    var exportWindow;
    var fileName;
    
    // Arrays for drop-down menus
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

    transparencyOptions = [
        "Solid",
        "Translucent",
        "Cutout",
        "Cutout (Mipped)"
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

    // Core menu component
    vueComponent = {
        data: {

            step: "start",
            error: false,

            // Data to be exported
            form: {
                namespace: "bamo",
                displayName: "",
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
            }
        },
        template: `
        <div class="wizardWrapper">

            <ul id="sideBar">
                <li v-bind:class="{selected: step =='start'}" @click.prevent="changePage($event, 'start')">
                    Start
                </li>
                <li class="sideBarHeader">Properties</li>
                <li v-bind:class="{selected: step == 'physical'}" @click.prevent="changePage($event, 'physical')">
                    Physical
                </li>
                <li v-bind:class="{selected: step == 'advPhys'}" @click.prevent="changePage($event, 'advPhys')">
                    Adv Physical
                </li>
                <li v-bind:class="{selected: step == 'other'}" @click.prevent="changePage($event, 'other')">
                    Other
                </li>
                <li class="sideBarHeader">Adv. Options</li>
                <li v-bind:class="{selected: step == 'settings'}" @click.prevent="changePage($event, 'settings')">
                    BAMO settings
                </li>
            </ul>

            <!-- Start -->
            <div class="wizardPage" v-if="step=='start'">
                <h1 style="padding:0; text-align: center;">BAMO Exporter</h1>
                <p align=center>Click "Export" at any time to export the model. Any unmodified properties will be set to the default values for a dirt block. Changes will persist until Blockbench is closed.</p>

                <ul class="settingsList">
                    <li>
                        <div class="headerLabel">Display Name</div>
                        <p class="headerDescription">The unique name of the block to be exported</p>
                        <p v-if="error==true" style="color:red">Block display name cannot be blank</p>
                        <input type="text" class="dark_bordered" style="" v-model="form.displayName" placeholder="blockname">
                    </li>
                </ul><br>
                
                <button @click.prevent="changePage($event, 'physical')">Continue</button>
                <button @click.prevent="createJSON" style = "float: right;">Export</button>
            </div>

            <!-- Physical Properties -->
            <div class="wizardPage" v-if="step=='physical'">
                <h1 style="padding:0; text-align: center;">Physical Properties</h1>
                <ul class="settingsList">
                    <li>
                        <div class = "headerLabel">Material</div>
                        <p class="headerDescription">Sets default properties to match those of the listed block. For a list of properties each block has, see the <a href="https://minecraft.fandom.com/wiki/Materials">Minecraft wiki</a></p>
                        <select class="dark_bordered" v-model="form.material"><option v-for="op in materialOptions" v-bind:value="op">{{ op }}</option></select>
                    </li>

                    <li>
                        <div class = "headerLabel">Block Sounds</div>
                        <p class="headerDescription">Sets the sounds made when the custom block is stepped on/broken/etc to match the listed block</p>
                        <select class="dark_bordered" v-model="form.sounds"><option v-for="op in soundOptions" v-bind:value="op">{{ op }}</option></select>
                    </li>

                    <li>
                        <div class="headerLabel">Transparency</div>
                        <p class="headerDescription">The transparency style of the block. Models with partially transparent textures should be set to Cutout</p>
                        <select class="dark_bordered" v-model="form.transparency"><option v-for="tr in transparencyOptions" v-bind:value="tr">{{ tr }}</option></select>
                    </li>
                    
                    <li>
                        <input type="checkbox" v-model="form.gravity">
                        <div class = "headerLabel">Gravity</div>
                        <p class="headerDescription">Determines if the block falls due to gravity</p>
                    </li>
                </ul>
                <button @click.prevent="changePage($event, 'start')">Previous</button>
                <button @click.prevent="changePage($event, 'advPhys')">Continue</button>
                <button @click.prevent="createJSON" style = "float: right;">Export</button>
            </div>
            
            <!-- Adv Physical Properties -->
            <div class="wizardPage" v-if="step=='advPhys'">
                <h1 style="padding:0; text-align: center;">Advanced Physical Properties</h1>
                <ul class="settingsList">
                    <li>
                        <input type="number" class="dark_bordered" v-model.number="form.lum" min=0 max=15>
                        <div class = "headerLabel">Luminance</div>
                        <p class="headerDescription">The light level of the block. Ranges from 0 to 15. Torches default at 14</p>
                    </li>

                    <li>
                        <input type="number" class="dark_bordered" v-model.number="form.blastRes" min=0>
                        <div class = "headerLabel">Blast Resistance</div>
                        <p class="headerDescription">How resistant the block is to explosions. Most blocks fall between 0.1 and 9</p>
                    </li>

                    <li>
                        <input type="number" class="dark_bordered" v-model.number="form.slip" min=0 step="0.1">
                        <div class = "headerLabel">Slipperiness</div>
                        <p class="headerDescription">How much you slide when crossing the block. Default is 0.6 and reaches 0.989 for blue ice</p>
                    </li>

                    <li>
                        <input type="checkbox" v-model="form.fireproof">
                        <div class = "headerLabel">Fireproof</div>
                        <p class="headerDescription">Determines if the block will burn</p>
                    </li>
                </ul>
                <button @click.prevent="changePage($event, 'physical')">Previous</button>
                <button @click.prevent="changePage($event, 'other')">Continue</button>
                <button @click.prevent="createJSON" style = "float: right;">Export</button>
            </div>

            <!-- Other Properties -->
            <div class="wizardPage" v-if="step=='other'">
                <h1 style="padding:0; text-align: center;">Other Properties</h1>
                <ul class="settingsList">
                   
                    <li>
                        <input type="number" class="dark_bordered" v-model.number="form.maxStack" min=0>
                        <div class = "headerLabel">Max Stack Size</div>
                        <p class="headerDescription">The maximum number the block in a stack</p>
                    </li>

                    <li>
                        <div class = "headerLabel">Creative Tab</div>
                        <p class="headerDescription">Which tab on the creative inventory menu the block will be added to</p>
                        <select class="dark_bordered" v-model="form.creativeTab"><option v-for="op in tabOptions" v-bind:value="op">{{ op }}</option></select>
                    </li>

                    <li>
                        <div class = "headerLabel">Rotation Type</div>
                        <p class="headerDescription">WIP: Currently not working</p>
                        <select class="dark_bordered" v-model="form.rotType"><option v-for="op in rotationTypes" v-bind:value="op">{{ op }}</option></select>
                    </li>

                    <li>
                        <div class = "headerLabel">Block Type</div>
                        <p class="headerDescription">What type of block (regular/stair/slab/etc) the model is (WIP)</p>
                    </li>
                <br>
                <button @click.prevent="changePage($event, 'advPhys')">Previous</button>
                <button @click.prevent="createJSON" style = "float: right;">Export</button>
            </div>

            <div class="wizardPage" v-if="step=='settings'">
                <h1 style="padding:0; text-align: center;">BAMO Export Settings</h1>
                <ul class="settingsList">
                    <li>
                        <div class = "headerLabel">Only export textures currently used</div>
                        <p class="headerDescription">WIP</p>
                    </li>

                    <li>
                        <div class = "headerLabel">reuse model file</div>
                        <p class="headerDescription">WIP</p>
                    </li>
                </ul>
            </div>
        </div>
        `,
        methods: {

            changePage: function(event, page){
                if (this.form.displayName == ""){
                    this.error = true;
                }else{
                    this.error = false;
                    this.step = page;
                }
            },

            reset: function(event){
                this.error = false;
                this.step = "start";
            },

            createJSON: function(event){

                if (this.form.displayName == ""){
                    this.error = true;
                    return;
                }

                // Define folder locations
                var dataFolder = settings.minecraftFolder.value + "\\bamo\\data\\";
                var blockstatesFolder = settings.minecraftFolder.value + "\\bamo\\assets\\" + this.form.namespace + "\\blockstates\\";
                var blockModelsFolder = settings.minecraftFolder.value + "\\bamo\\assets\\" + this.form.namespace + "\\models\\block\\";
                var itemModelsFolder = settings.minecraftFolder.value + "\\bamo\\assets\\" + this.form.namespace + "\\models\\item\\";
                var blockTexturesFolder = settings.minecraftFolder.value + "\\bamo\\assets\\" + this.form.namespace + "\\textures\\blocks\\";

                // Create the folders if they dont exist
                var folderList = [dataFolder, blockstatesFolder, blockModelsFolder, itemModelsFolder, blockTexturesFolder];
                var fs = require('fs');

                folderList.forEach(function(folder){
                    if (!fs.existsSync(folder)){
                        fs.mkdirSync(folder, {recursive: true});
                    }
                })

                // Create mcmeta file if it doesnt exist
                if (!fs.existsSync(settings.minecraftFolder.value + "\\bamo\\pack.mcmeta")){
                    mcmetaData = {"pack" : {"pack_format" : 6, "description" : "Resource Pack for BAMO test files"}};
                    fs.writeFile(settings.minecraftFolder.value + "\\bamo\\pack.mcmeta", JSON.stringify(mcmetaData), "utf8", (err) => {if (err != null) {console.log("Error generating mcmeta file:", err);}});
                }

                // Generate block name from the displayname
                var blockName = this.form.displayName.replace(/\s+/g, '').toLowerCase();

                // Create blockstates files
                var stateData = {"variants" : {"" : {"model" : this.form.namespace + ":block/" + blockName}}};
                fs.writeFile(blockstatesFolder + "\\" + blockName + ".json", JSON.stringify(stateData), "utf8", (err) => {if (err != null) {console.log("Error generating blockstate:", err);}});

                // Pull the model data from the codec
                var modelData = JSON.parse(Format.codec.compile());

                // Add the parent data
                modelData["parent"] = "block/block";

                // Add namespace to textures if needed
                Object.keys(modelData.textures).forEach((key) => {
                    if (modelData.textures[key].includes(":") == false){
                        modelData.textures[key] = this.form.namespace + ":blocks/" + modelData.textures[key];
                    }else{
                        // KINDA CRUDE, NEEDS UPDATING
                        splitTexture = modelData.textures[key].split(":");
                        imageName = splitTexture[1].split("/")
                        modelData.textures[key] = this.form.namespace + ":blocks/" + imageName.pop();
                    }
                })

                // Write model files
                fs.writeFile(blockModelsFolder + "\\" + blockName + ".json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                fs.writeFile(itemModelsFolder + "\\" + blockName + ".json", JSON.stringify(modelData), "utf8", err => {if (err != null) {console.log("Error Found writing item model data:", err);}});

                // Copy texture files
                Texture.all.forEach(function(tx){
                    var image = nativeImage.createFromPath(tx.source.replace(/\?\d+$/, '')).toPNG();
                    fs.writeFile(blockTexturesFolder + "\\" + tx.name, image, (err) => {if (err != null) {console.log("Error Found writing texture data:", err);}});
                })
                
                // Write block properties file

                var data = {
                    "displayName" : blockName, //this.form.displayName,
                    "material" : this.form.material,
                    "blastRes" : this.form.blastRes,
                    "slip" : this.form.slip,
                    "gravity" : this.form.gravity,
                    "rotType" : this.form.rotType,
                    "sounds" : this.form.sounds,
                    "lum" : this.form.lum,
                    "maxStack" : this.form.maxStack,
                    "fireproof" : this.form.fireproof,
                    "creativeTab" : this.form.creativeTab,
                    "transparency": this.form.transparency,
                };

                fs.writeFile(dataFolder + "\\" + blockName + ".json", JSON.stringify(data), "utf8", err => {if (err != null) {console.log("Error writing block properties:", err);}});

                let e = open_interface;
                e.hide();
            },
        }
    }
    
    var cssData;

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
            });
            
            // Export button in menu
            btn = new Action('block_mod', {
                name: 'BAMO',
                description: 'Exports block metadata for BAMO mod',
                icon: 'fa-cube',
                click: function () {
                    if (Project.name != undefined){
                        if ((Settings.get('minecraftFolder') != undefined) && (Settings.get('minecraftFolder') != '')){
                            exportWindow.show();
                            //exportWindow.content_vue.reset();
                        }else{
                            Blockbench.showMessageBox({buttons: ["Ok"], title: "Error", message: "You must set your Resources folder location under Settings->Export"});
                        }
                    }else{
                        Blockbench.showMessageBox({buttons: ["Ok"], title: "Error", message: "Please ensure your file is saved before exporting. If you see this for a saved file, reload it and try again"});
                    }
                }
            });

            MenuBar.addAction(btn, 'file');

            // Dialog that opens when you click the button1
            // See the vueComponent object for details
            exportWindow = new Dialog('C&CExportWindow', {
                id: "BAMO",
                title: 'BAMO Exporter',
                component: vueComponent,
                buttons: [],
                padding: !1,
                width: 720,
                height: 620,
            });

            cssData = Blockbench.addCSS(`
                #BAMO .dialog_content {
                    margin: 0;
                } 

                #BAMO .wizardWrapper {
                    display: flex; 
                    flex-direction: row;
                    height: 100%;
                } 

                #BAMO .wizardPage {
                    width: 100%; 
                    padding: 12px 20px;
                } 

                #BAMO .headerLabel {
                    font-size: 20px; 
                    display: inline-block
                } 

                #BAMO .headerDescription {
                    color: white;
                } 
                
                #BAMO input[type='text'] {
                    display: block; 
                    width: 100%; 
                    height: auto; 
                    padding: 4px 8px; 
                    border: 1px solid black;
                } 
                
                #BAMO input[type='number']{
                    display: inline-block; 
                    width: 50px; 
                    height: auto; 
                    border: 1px solid black; 
                    padding-left: 4px; 
                    margin-right: 8px; 
                    text-align: right;
                } 
                
                #BAMO input[type='checkbox'] {
                    padding-right: 8px;
                } 
                
                #BAMO select {
                    width: 100%; 
                    height: auto; 
                    padding: 4px 8px;
                    border: 1px solid black; 
                } 
                
                #sideBar {
                    border-right: 2px solid black;
                    width: 175px;
                } 
                
                #sideBar li {
                    width: 100%; 
                    padding: 8px 20px; 
                    cursor: pointer;
                } 
                
                #sideBar li.selected {
                    width: 100%; 
                    padding: 8px 20px; 
                    border-right: 4px solid green; 
                    cursor: default;
                    background-color: gray;
                } 

                #sideBar li.sideBarHeader {
                    width: 100%;
                    padding: 2px 10px;
                    font-size: 12px;
                    cursor: default;
                }
                
                ul.settingsList li {
                    padding: 8px 0;
                }
            `);

            // Pull the filename when loading/saving to use for file dialog
            Blockbench.on("load_project", function() {
                fileName = Project.name;
                //exportWindow.content_vue.changeDisplayName(Project.name);
            });

            Blockbench.on("save_project", function() {
                if (fileName != Project.name){
                    fileName = Project.name;
                    //exportWindow.content_vue.changeDisplayName(fileName);
                }
               // exportWindow.content_vue.displayName = fileName;
            });
        },
        
        onunload() {
            btn.delete();
            minecraftFolder.delete();
            cssData.delete();
        }
    });

})();