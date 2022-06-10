export function imageNameToTexture(namespace, type, image){
    return namespace + ":" + type + "/" + image.name.split(".")[0]
}