/**
 * @return {Random}
 * @constructor
 */
function Random(){

}

/**
 *
 * @return {double}
 */
Random.nextDouble=function(){
    return Math.random()
}

/**
 * 0~0x7fffffff
 * @return {int}
 */
Random.nextInt=function (){
    return Math.abs(Math.round((Math.random()*0x7fff *Math.pow(2,16)+Math.random()*0x7fff)))
}

/**
 * 0~0x7fff
 * @return {int}
 */
Random.nextShort=function (){
    return Math.abs(Math.round(Math.random()*0x7fff))
}

/**
 * 0~max
 * @param max {int}
 * @return {number}
 */
Random.nextLowerInt=function(max){
    return Random.nextInt()%max
}

/**
 *
 * @return {boolean}
 */
Random.nextBoolean=function(){
    return Math.random()>0.5
}

/**
 *
 * @param min {int}
 * @param max {int}
 * @return {int}
 */
Random.nextBetweenInt=function(min,max){
    return Random.nextInt() % (max - min) + min
}


/**
 *
 * @return {double}
 */
Random.prototype.nextDouble=function(){
    return Random.nextDouble()
}

/**
 * 0~0x7fffffff
 * @return {int}
 */
Random.prototype.nextInt=function (){
    return Random.nextInt()
}

/**
 * 0~0x7fff
 * @return {int}
 */
Random.prototype.nextShort=function (){
    return Random.nextShort()
}

/**
 * 0~max
 * @param max {int}
 * @return {number}
 */
Random.prototype.nextLowerInt=function(max){
    return Random.nextLowerInt(max)
}

/**
 *
 * @return {boolean}
 */
Random.prototype.nextBoolean=function(){
    return Random.nextBoolean()
}

/**
 *
 * @param min {int}
 * @param max {int}
 * @return {int}
 */
Random.prototype.nextBetweenInt=function(min,max){
    return Random.nextBetweenInt(min,max)
}

export default Random
