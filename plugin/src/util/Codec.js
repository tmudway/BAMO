import bamoSettings, { BAMO_SETTINGS_DEFAULT } from "./Settings";

export function loadCodec(){
    Codecs.project.on('compile', onProjectCompile);
    Codecs.project.on('parse', onProjectParse);
    Blockbench.on('close_project', onCloseProject);
}

export function unloadCodec(){
    Codecs.project.events.compile.remove(onProjectCompile)
    Codecs.project.events.parse.remove(onProjectParse)
    format.delete();
}

function onProjectCompile(e){
    if (format.id !== "bamo_model") return;
    e.model.bamoSettings = bamoSettings[Project.uuid];
}

function onProjectParse(e){
    if (e.model && typeof e.model.bamoSettings === 'object'){
        let placeholder = Object.assign({}, BAMO_SETTINGS_DEFAULT);
        bamoSettings[Project.uuid] = Object.assign(placeholder, e.model.bamoSettings);
    }else{
        bamoSettings[Project.uuid] = Object.assign({}, BAMO_SETTINGS_DEFAULT);
        bamoSettings[Project.uuid].displayName = Project.name
    }
    console.log(bamoSettings[Project.uuid]);
}

function onCloseProject(){
    delete bamoSettings[Project.uuid]
}

var codec = Codecs.bbmodel;

var format = new ModelFormat({
    id: "bamo_model",
    name: "BAMO model",
    category: "minecraft",
    description: "A format for storing models to be exported for the BAMO mod",
    icon: "view_in_ar",
    select_texture_for_particles: true,
    rotate_cubes: true,
    box_uv: true,
    optional_box_uv: true,
    bone_rig: true,
    centered_grid: true,
    animated_textures: true,
    animation_mode: true,
    animation_files: true,
    locators: true,
    codec: Codecs.project
});

export default codec