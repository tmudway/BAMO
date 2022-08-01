export function imageNameToTexture(namespace, type, image){
    var nm = ""
    if (image.name){
        nm = image.name.split(".")[0].replace(/[^a-zA-Z\d\s.]/g, '').replace(/\s+/g, "_").toLowerCase()
    }else{
        nm = image.split(".")[0].replace(/[^a-zA-Z\d\s.]/g, '').replace(/\s+/g, "_").toLowerCase()
    }
    return namespace + ":" + type + "/" + nm
}