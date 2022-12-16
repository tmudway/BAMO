export const BAMO_SETTINGS_DEFAULT = {
    displayName: "",
    namespace: "bamo",
    typeList: [],
    material: "Dirt",
    blastRes: 6,
    slip: 0.6,
    gravity: false,
    rotType: "default",
    sounds: "Grass",
    lum: 0,
    maxStack: 64,
    fireproof: true,
    creativeTab: "Building Blocks",
    transparency: "Solid",
    blockType: "Default",
    types: {
        custom: true,
        customType: "Default",
        block: false,
        stair: false,
        slab: false,
        wall: false,
    },
    variant: {
        default:{
            all: "",
        },
        stair: {
            top: "",
            bottom: "",
            side: "",
            particle: ""
        },
        slab: {
            top: "",
            bottom: "",
            side: "",
            particle: ""
        },
        wall: {
            wall: "",
            particle: ""
        }
    },
    particles: false,
    particleType: "Smoke",
    particlePos: {
        x: 8,
        y: 8,
        z: 8
    }
};

Object.freeze(BAMO_SETTINGS_DEFAULT);

let bamoSettings = {}
bamoSettings[""] =  Object.assign({}, BAMO_SETTINGS_DEFAULT);

export default bamoSettings

