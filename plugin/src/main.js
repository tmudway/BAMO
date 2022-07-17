import BamoComponent from './components/BamoComponent.vue';

const css = require('./components/bamo.css').toString();

let btn;
let exportWindow;
let cssData;
let minecraftFolder

Plugin.register('BAMO', {
	title: 'BAMO Exporter',
	author: 'Ryytikki',
	description: 'Exports block metadata for use in the BAMO mod',
	icon: 'bar_chart',
	version: '0.3.2',
	variant: 'both',
	
	onload() {

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
			name: 'BAMO Export',
			description: 'Exports block metadata for BAMO mod',
			icon: 'fa-cube',
			click: function () {
				exportWindow.show();
			}
			/*	if (Project.name != undefined){
					if ((Settings.get('minecraftFolder') != undefined) && (Settings.get('minecraftFolder') != '')){
						exportWindow.show();
						//exportWindow.content_vue.reset();
					}else{
						Blockbench.showMessageBox({buttons: ["Ok"], title: "Error", message: "You must set your Minecraft folder location under Settings->Export"});
					}
				}else{
					Blockbench.showMessageBox({buttons: ["Ok"], title: "Error", message: "Please ensure your file is saved before exporting. If you see this for a saved file, reload it and try again"});
				}
			}*/
		});

		MenuBar.addAction(btn, 'file');

		// Dialog that opens when you click the button1
		// See the BamoComponent object for details
		exportWindow = new Dialog('BAMOExportWindow', {
			id: "BAMO",
			title: 'BAMO Exporter',
			component: BamoComponent,
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
	}
});
