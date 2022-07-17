export function imageNameToTexture(namespace, type, image){
    var nm = ""
    if (image.name){
        nm = image.name.split(".")[0]
    }else{
        nm = image.split(".")[0]
    }
    return namespace + ":" + type + "/" + nm
}