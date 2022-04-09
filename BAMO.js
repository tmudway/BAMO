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


    function genStairState(namespace, model, outer, inner){
        return `
        {
            "variants": {
                "facing=east,half=bottom,shape=straight": { "model": "${namespace}:${model}" },
                "facing=west,half=bottom,shape=straight": { "model": "${namespace}:${model}", "y": 180, "uvlock": true },
                "facing=south,half=bottom,shape=straight": { "model": "${namespace}:${model}", "y": 90, "uvlock": true },
                "facing=north,half=bottom,shape=straight": { "model": "${namespace}:${model}", "y": 270, "uvlock": true },

                "facing=east,half=top,shape=straight": { "model": "${namespace}:${model}", "x": 180, "uvlock": true },
                "facing=west,half=top,shape=straight": { "model": "${namespace}:${model}", "x": 180, "y": 180, "uvlock": true },
                "facing=south,half=top,shape=straight": { "model": "${namespace}:${model}", "x": 180, "y": 90, "uvlock": true },
                "facing=north,half=top,shape=straight": { "model": "${namespace}:${model}", "x": 180, "y": 270, "uvlock": true },
                
                "facing=east,half=bottom,shape=outer_right": { "model": "${namespace}:${outer}" },
                "facing=west,half=bottom,shape=outer_right": { "model": "${namespace}:${outer}", "y": 180, "uvlock": true },
                "facing=south,half=bottom,shape=outer_right": { "model": "${namespace}:${outer}", "y": 90, "uvlock": true },
                "facing=north,half=bottom,shape=outer_right": { "model": "${namespace}:${outer}", "y": 270, "uvlock": true },

                "facing=east,half=bottom,shape=outer_left": { "model": "${namespace}:${outer}", "y": 270, "uvlock": true },
                "facing=west,half=bottom,shape=outer_left": { "model": "${namespace}:${outer}", "y": 90, "uvlock": true },
                "facing=south,half=bottom,shape=outer_left": { "model": "${namespace}:${outer}" },
                "facing=north,half=bottom,shape=outer_left": { "model": "${namespace}:${outer}", "y": 180, "uvlock": true },

                "facing=east,half=bottom,shape=inner_right": { "model": "${namespace}:${inner}" },
                "facing=west,half=bottom,shape=inner_right": { "model": "${namespace}:${inner}", "y": 180, "uvlock": true },
                "facing=south,half=bottom,shape=inner_right": { "model": "${namespace}:${inner}", "y": 90, "uvlock": true },
                "facing=north,half=bottom,shape=inner_right": { "model": "${namespace}:${inner}", "y": 270, "uvlock": true },

                "facing=east,half=bottom,shape=inner_left": { "model": "${namespace}:${inner}", "y": 270, "uvlock": true },
                "facing=west,half=bottom,shape=inner_left": { "model": "${namespace}:${inner}", "y": 90, "uvlock": true },
                "facing=south,half=bottom,shape=inner_left": { "model": "${namespace}:${inner}" },
                "facing=north,half=bottom,shape=inner_left": { "model": "${namespace}:${inner}", "y": 180, "uvlock": true },
               
                "facing=east,half=top,shape=outer_left": { "model": "${namespace}:${outer}", "x": 180, "uvlock": true },
                "facing=west,half=top,shape=outer_left": { "model": "${namespace}:${outer}", "x": 180, "y": 180, "uvlock": true },
                "facing=south,half=top,shape=outer_left": { "model": "${namespace}:${outer}", "x": 180, "y": 90, "uvlock": true },
                "facing=north,half=top,shape=outer_left": { "model": "${namespace}:${outer}", "x": 180, "y": 270, "uvlock": true },

                "facing=east,half=top,shape=outer_right": { "model": "${namespace}:${outer}", "x": 180, "y": 90, "uvlock": true },
                "facing=west,half=top,shape=outer_right": { "model": "${namespace}:${outer}", "x": 180, "y": 270, "uvlock": true },
                "facing=south,half=top,shape=outer_right": { "model": "${namespace}:${outer}", "x": 180, "y": 180, "uvlock": true },
                "facing=north,half=top,shape=outer_right": { "model": "${namespace}:${outer}", "x": 180, "uvlock": true },

                "facing=east,half=top,shape=inner_left": { "model": "${namespace}:${inner}", "x": 180, "uvlock": true },
                "facing=west,half=top,shape=inner_left": { "model": "${namespace}:${inner}", "x": 180, "y": 180, "uvlock": true },
                "facing=south,half=top,shape=inner_left": { "model": "${namespace}:${inner}", "x": 180, "y": 90, "uvlock": true },
                "facing=north,half=top,shape=inner_left": { "model": "${namespace}:${inner}", "x": 180, "y": 270, "uvlock": true },

                "facing=east,half=top,shape=inner_right": { "model": "${namespace}:${inner}", "x": 180, "y": 90, "uvlock": true },
                "facing=west,half=top,shape=inner_right": { "model": "${namespace}:${inner}", "x": 180, "y": 270, "uvlock": true },
                "facing=south,half=top,shape=inner_right": { "model": "${namespace}:${inner}", "x": 180, "y": 180, "uvlock": true },
                "facing=north,half=top,shape=inner_right": { "model": "${namespace}:${inner}", "x": 180, "uvlock": true }
            }
        }`
    }

    function genWallState(namespace, post, side, tall){
        return `{
            "multipart":[
                {"when":{"up":"true"},"apply":{"model":"${namespace}:${post}"}},
                {"when":{"north":"low"},"apply":{"model":"${namespace}:${side}","uvlock":true}},
                {"when":{"east":"low"},"apply":{"model":"${namespace}:${side}","y":90,"uvlock":true}},
                {"when":{"south":"low"},"apply":{"model":"${namespace}:${side}","y":180,"uvlock":true}},
                {"when":{"west":"low"},"apply":{"model":"${namespace}:${side}","y":270,"uvlock":true}},
                {"when":{"north":"tall"},"apply":{"model":"${namespace}:${tall}","uvlock":true}},
                {"when":{"east":"tall"},"apply":{"model":"${namespace}:${tall}","y":90,"uvlock":true}},
                {"when":{"south":"tall"},"apply":{"model":"${namespace}:${tall}","y":180,"uvlock":true}},
                {"when":{"west":"tall"},"apply":{"model":"${namespace}:${tall}","y":270,"uvlock":true}}
            ]
        }`
    }

    function imageNameToTexture(namespace, type, image){
        return namespace + ":" + type + "/" + image.name.split(".")[0]
    }

    // Core menu component
    vueComponent = {
        data: {

            step: "start",
            error: false,

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
                    top: Texture.all[0],
                    bottom: Texture.all[0],
                    side: Texture.all[0],
                    particle: Texture.all[0]
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
        },
        template: `
        <div class="wizardWrapper">

            <ul id="sideBar">
                <li v-bind:class="{selected: step =='start'}" @click.prevent="changePage($event, 'start')">
                    Start
                </li>
                <li class="sideBarHeader">Blocks</li>
                <li v-bind:class="{selected: step =='types'}" @click.prevent="changePage($event, 'types')">
                    Block Types
                </li>
                <li v-bind:class="{selected: step =='variant'}" v-if="types.block" @click.prevent="changePage($event, 'variant')">
                    Variants
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
                        <input type="text" class="dark_bordered" style="" v-model="properties.displayName" placeholder="blockname">
                    </li>
                </ul><br>
                
                <button @click.prevent="changePage($event, 'types')">Continue</button>
                <button @click.prevent="createJSON" style = "float: right;">Export</button>
            </div>

            <!-- Block Types -->
            <div class="wizardPage" v-if="step=='types'">
                <h1 style="padding:0; text-align: center;">Block Types</h1>
                <ul class="settingsList">
                    <li>
                        <input type="checkbox" v-model="types.custom" v-on:change="toggleType($event, 'custom')">
                        <div class = "headerLabel">Custom Block</div>
                        <p class="headerDescription">Block with a custom model</p>
                    </li>
                    <li>
                        <input type="checkbox" v-model="types.block" v-on:change="toggleType($event, 'block')">
                        <div class = "headerLabel">Standard Block</div>
                        <p class="headerDescription">Standard minecraft block with identical textures on all faces</p>
                    </li>
                </ul><br>

                <button @click.prevent="changePage($event, 'start')">Previous</button>
                <button @click.prevent="changePage($event, 'physical')">Continue</button>
                <button @click.prevent="createJSON" style = "float: right;">Export</button>
            </div>

            <!-- Variant Block -->
            <div class="wizardPage" v-if="step=='variant'">
                <h1 style="padding:0; text-align: center;">Variant Blocks</h1>
                <ul class="settingsList">
                    <li>
                        <input type="checkbox" v-model="types.stair">
                        <div class = "headerLabel">Create Stair Block</div>
                    </li>
                    <li style="padding-left: 20px" v-if="types.stair">
                        <div class = "headerLabel">Top Texture</div>
                        <select class="dark_bordered" v-model="variant.stair.top" >
                            <option v-for="op in Texture.all" v-bind:value="op">{{op.name}}</option>
                        </select>
                    </li>
                    <li style="padding-left: 20px" v-if="types.stair"> 
                        <div class = "headerLabel">Bottom Texture</div>
                        <select class="dark_bordered" v-model="variant.stair.bottom">
                            <option v-for="op in Texture.all" v-bind:value="op">{{op.name}}</option>
                        </select>
                    </li>
                    <li style="padding-left: 20px" v-if="types.stair"> 
                        <div class = "headerLabel">Side Texture</div>
                        <select class="dark_bordered" v-model="variant.stair.side">
                            <option v-for="op in Texture.all" v-bind:value="op">{{op.name}}</option>
                        </select>
                    </li>
                    
                    <li>
                        <input type="checkbox" v-model="types.slab">
                        <div class = "headerLabel">Create Slab Block</div>
                    <li style="padding-left: 20px" v-if="types.slab">
                        <div class = "headerLabel">Top Texture</div>
                        <select class="dark_bordered" v-model="variant.slab.top" >
                            <option v-for="op in Texture.all" v-bind:value="op">{{op.name}}</option>
                        </select>
                    </li>
                    <li style="padding-left: 20px" v-if="types.slab"> 
                        <div class = "headerLabel">Bottom Texture</div>
                        <select class="dark_bordered" v-model="variant.slab.bottom">
                            <option v-for="op in Texture.all" v-bind:value="op">{{op.name}}</option>
                        </select>
                    </li>
                    <li style="padding-left: 20px" v-if="types.slab"> 
                        <div class = "headerLabel">Side Texture</div>
                        <select class="dark_bordered" v-model="variant.slab.side">
                            <option v-for="op in Texture.all" v-bind:value="op">{{op.name}}</option>
                        </select>
                    </li>

                    <li>
                        <input type="checkbox" v-model="types.wall">
                        <div class = "headerLabel">Create Wall Block</div>
                    </li>
                    <li style="padding-left: 20px" v-if="types.wall">
                        <div class = "headerLabel">Wall Texture</div>
                        <select class="dark_bordered" v-model="variant.wall.wall" >
                            <option v-for="op in Texture.all" v-bind:value="op">{{op.name}}</option>
                        </select>
                    </li>
                </ul><br>
                <button @click.prevent="changePage($event, 'types')">Previous</button>
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
                        <select class="dark_bordered" v-model="properties.material"><option v-for="op in materialOptions" v-bind:value="op">{{ op }}</option></select>
                    </li>

                    <li>
                        <div class = "headerLabel">Block Sounds</div>
                        <p class="headerDescription">Sets the sounds made when the custom block is stepped on/broken/etc to match the listed block</p>
                        <select class="dark_bordered" v-model="properties.sounds"><option v-for="op in soundOptions" v-bind:value="op">{{ op }}</option></select>
                    </li>

                    <li>
                        <div class="headerLabel">Transparency</div>
                        <p class="headerDescription">The transparency style of the block. Models with partially transparent textures should be set to Cutout</p>
                        <select class="dark_bordered" v-model="properties.transparency"><option v-for="tr in transparencyOptions" v-bind:value="tr">{{ tr }}</option></select>
                    </li>
                    
                    <li>
                        <input type="checkbox" v-model="properties.gravity">
                        <div class = "headerLabel">Gravity</div>
                        <p class="headerDescription">Determines if the block falls due to gravity</p>
                    </li>
                </ul>
                <button @click.prevent="changePage($event, 'types')">Previous</button>
                <button @click.prevent="changePage($event, 'advPhys')">Continue</button>
                <button @click.prevent="createJSON" style = "float: right;">Export</button>
            </div>
            
            <!-- Adv Physical Properties -->
            <div class="wizardPage" v-if="step=='advPhys'">
                <h1 style="padding:0; text-align: center;">Advanced Physical Properties</h1>
                <ul class="settingsList">
                    <li>
                        <input type="number" class="dark_bordered" v-model.number="properties.lum" min=0 max=15>
                        <div class = "headerLabel">Luminance</div>
                        <p class="headerDescription">The light level of the block. Ranges from 0 to 15. Torches default at 14</p>
                    </li>

                    <li>
                        <input type="number" class="dark_bordered" v-model.number="properties.blastRes" min=0>
                        <div class = "headerLabel">Blast Resistance</div>
                        <p class="headerDescription">How resistant the block is to explosions. Most blocks fall between 0.1 and 9</p>
                    </li>

                    <li>
                        <input type="number" class="dark_bordered" v-model.number="properties.slip" min=0 step="0.1">
                        <div class = "headerLabel">Slipperiness</div>
                        <p class="headerDescription">How much you slide when crossing the block. Default is 0.6 and reaches 0.989 for blue ice</p>
                    </li>

                    <li>
                        <input type="checkbox" v-model="properties.fireproof">
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
                        <input type="number" class="dark_bordered" v-model.number="properties.maxStack" min=0>
                        <div class = "headerLabel">Max Stack Size</div>
                        <p class="headerDescription">The maximum number the block in a stack</p>
                    </li>

                    <li>
                        <div class = "headerLabel">Creative Tab</div>
                        <p class="headerDescription">Which tab on the creative inventory menu the block will be added to</p>
                        <select class="dark_bordered" v-model="properties.creativeTab"><option v-for="op in tabOptions" v-bind:value="op">{{ op }}</option></select>
                    </li>

                    <li>
                        <div class = "headerLabel">Rotation Type</div>
                        <p class="headerDescription">WIP: Currently not working</p>
                        <select class="dark_bordered" v-model="properties.rotType"><option v-for="op in rotationTypes" v-bind:value="op">{{ op }}</option></select>
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
                        <div class = "headerLabel">Set Namespace</div>
                        <p class="headerDescription">WIP</p>
                    </li>

                    <li>
                        <div class = "headerLabel">Only export textures currently used</div>
                        <p class="headerDescription">WIP</p>
                    </li>

                    <li>
                        <div class = "headerLabel">reuse model file</div>
                        <p class="headerDescription">WIP</p>
                    </li>

                    <li>
                        <div class = "headerLabel">show warning if model with same name exists</div>
                        <p class="headerDescription">WIP</p>
                        <div class = "headerLabel" style="padding-left: 40px;">Only do this if file is closed/new file loaded</div>
                    </li>
                </ul>
            </div>
        </div>
        `,
        methods: {

            changePage: function(event, page){
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

            toggleType: function(event, type){
                
                if (this.types.custom && type=="custom"){
                    this.types.block = false
                    return
                }

                if (this.types.block && type=="block"){
                    this.types.custom = false
                    return
                }
            },

            reset: function(event){
                this.error = false;
                this.step = "start";
            },

            createJSON: function(event){

                if (this.properties.displayName == ""){
                    this.error = true;
                    return;
                }

                var packName = "bamo";

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
                    mcmetaData = {"pack" : {"pack_format" : 6, "description" : "Resource Pack for BAMO test files"}};
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
                    // Create blockstates files
                    var stateData = JSON.stringify({"variants" : {"" : {"model" : this.properties.namespace + ":block/" + blockName}}});

                    // Add namespace to textures if needed
                    Object.keys(modelData.textures).forEach((key) => {
                        if (modelData.textures[key].includes(":") == false){
                            modelData.textures[key] = this.properties.namespace + ":blocks/" + modelData.textures[key];
                        }else{
                            // KINDA CRUDE, NEEDS UPDATING
                            splitTexture = modelData.textures[key].split(":");
                            imageName = splitTexture[1].split("/");
                            modelData.textures[key] = this.properties.namespace + ":blocks/" + imageName.pop();
                        }
                    })

                    blockList.push({"name": blockName, "types": [], "model": modelData, "state": stateData});
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

                    blockList.push({"name": blockName, "types": typeList, "state": state, "model": modelData});
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

                    if (!fs.existsSync(wallTags)){
                        fs.mkdirSync(wallTags, {recursive: true});
                        tagData = {"replace" : false, "values": [tagVal]};
                    }else{
                        d = fs.readFileSync(wallTags);
                        tagData = JSON.parse(d);
                        if (!tagData["values"].includes(tagVal)){
                            tagData["values"].push(tagVal);
                        }
                    }   
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
        
                    fs.writeFile(blockTexturesFolder + "\\" + tx.name, image, (err) => {if (err != null) {console.log("Error Found writing texture data:", err);}});
                })

                blockList.forEach(block => {
                    fs.writeFile(blockstatesFolder + "\\" + block["name"] + ".json", block["state"], "utf8", (err) => {if (err != null) {console.log("Error generating blockstate:", err);}});

                    // Write model files
                    fs.writeFile(blockModelsFolder + "\\" + block["name"] + ".json", JSON.stringify(block["model"]), "utf8", err => {if (err != null) {console.log("Error Found writing block model data:", err);}});
                    fs.writeFile(itemModelsFolder + "\\" + block["name"] + ".json", JSON.stringify(block["model"]), "utf8", err => {if (err != null) {console.log("Error Found writing item model data:", err);}});
                    
                    // Write block properties file
                    var data = {
                        "displayName" : block["name"], //this.properties.displayName,
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
                    };

                    fs.writeFile(objFolder + "\\" + block["name"] + ".json", JSON.stringify(data), "utf8", err => {if (err != null) {console.log("Error writing block properties:", err);}});
                })
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
                name : 'Minecraft Folder Location',
                description: 'Location of the Minecraft folder on your PC',
                category: 'export',
                type: 'text',
                value: ''
            });
            
            // Export button in menu
            btn = new Action('block_mod', {
                name: 'BAMO Export',
                description: 'Exports block metadata for BAMO mod',
                icon: 'fa-cube',
                click: function () {
                    if (Project.name != undefined){
                        if ((Settings.get('minecraftFolder') != undefined) && (Settings.get('minecraftFolder') != '')){
                            exportWindow.show();
                            //exportWindow.content_vue.reset();
                        }else{
                            Blockbench.showMessageBox({buttons: ["Ok"], title: "Error", message: "You must set your Minecraft folder location under Settings->Export"});
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