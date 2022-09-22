export function genLootTable(namespace, block, variants){
    if (variants.length == 0){
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
    }else{

    }
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
    }
    
}