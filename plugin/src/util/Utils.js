export function imageNameToTexture(namespace, type, image){
    var nm = ""
    if (image.name){
        nm = image.name.split(".")[0].replace(/[^a-zA-Z\d\s.]/g, '').replace(/\s+/g, "_").toLowerCase()
    }else{
        nm = image.split(".")[0].replace(/[^a-zA-Z\d\s.]/g, '').replace(/\s+/g, "_").toLowerCase()
    }
    return namespace + ":" + type + "/" + nm
}

export function dictFromTexture(image, ns){
    var ret = ""
    Texture.all.forEach(function(tx){
        if ((tx.name == image) || (image == "particle" && tx.particle == true)){
            if (tx.namespace == ""){
                ret = ns + ":blocks/" + tx.name.split(".")[0].replace(/[^a-zA-Z\d\s.]/g, '').replace(/\s+/g, "_").toLowerCase()
            }else{
                ret = tx.namespace + ":" + tx.folder + "/" + tx.name.split(".")[0].replace(/[^a-zA-Z\d\s.]/g, '').replace(/\s+/g, "_").toLowerCase()
            }
        }
    })

    return ret
}