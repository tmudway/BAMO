export function genLootTable(namespace, block){

        return  `{
    "type": "minecraft:block",
    "pools": [
        {
            "rolls": 1,
            "entries": [
                {
                    "type": "minecraft:item",
                    "name": "${namespace}:${block}"
                }
            ],
            "conditions": []
        }
    ]
}`

}

export function genMineableTag(namespace, block, variants){
    if (variants.length == 0){
        return `{
    "replace": false,
    "values": [
        "${namespace}:${block}"
    ]
}`

    }else{

        var tagValues = [`${namespace}:${block}`]
        variants.forEach(function(v){
            tagValues.push(`${namespace}:${block}_${v}`)
        })

        var data = {
            "replace": false,
            "values": tagValues
        }

        return JSON.stringify(data)
    }
    
}