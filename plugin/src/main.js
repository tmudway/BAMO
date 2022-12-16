import BamoBaseComponent from './components/BamoBaseComponent.vue';
import codec, { loadCodec, unloadCodec } from './util/Codec';
import bamoSettings, {BAMO_SETTINGS_DEFAULT} from './util/Settings';
const css = require('./components/bamo.css').toString();

let btn;
let exportWindow;
let cssData;
let minecraftFolder;

Plugin.register('BAMO', {
	title: 'BAMO Exporter',
	author: 'Ryytikki',
	description: 'Exports block metadata for use in the BAMO mod',
	icon: 'bar_chart',
	version: '0.3.4',
	variant: 'both',
	
	onload() {

		loadCodec();

		// Setting that holds the resource pack folder location
		minecraftFolder = new Setting('minecraftFolder', {
			name : 'Minecraft Folder Location',
			description: 'Location of the Minecraft folder on your PC',
			category: 'export',
			type: 'text',
			value: ''
		});

		// Export button in menu
		btn = new Action('bamo', {
			name: 'BAMO Properties & Export',
			description: 'Sets block properties for BAMO mod',
			icon: 'fa-cube',
			click: function () {
				if (Project.name != undefined){
					if (Texture.all.length == 0){
						Blockbench.showMessageBox({buttons: ["Ok"], title: "Error", message: "Please create a texture"});
						return;
					}
					
					// If the bamoSettings for the file havent been generated, generate
					if (typeof bamoSettings[Project.uuid] !== 'object'){
						bamoSettings[Project.uuid] = Object.assign({}, BAMO_SETTINGS_DEFAULT);
        				bamoSettings[Project.uuid].displayName = Project.name
					}
					

					if ((Settings.get('minecraftFolder') != undefined) && (Settings.get('minecraftFolder') != '')){
						exportWindow.show();
						exportWindow.content_vue.updateValues()
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
		// See the BamoComponent object for details
		exportWindow = new Dialog('BAMOExportWindow', {
			id: "BAMO",
			title: 'BAMO Exporter',
			component: BamoBaseComponent,
			buttons: [],
			padding: !1,
			width: 720,
			height: 620,
		});

		cssData = Blockbench.addCSS(css);
	},

	onunload() {
		btn.delete();
		minecraftFolder.delete();
		cssData.delete();
		unloadCodec();
	}
});
