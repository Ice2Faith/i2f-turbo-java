/**
 * 随机工具
 */
const Random = {
    /**
     *
     * @return double 0-1
     */
    nextDouble() {
        return Math.random()
    },
    /**
     *
     * @return int 0-0x7fff
     */
    nextInt() {
        return Math.floor(this.nextDouble() * 0x7fff)
    },
    /**
     *
     * @param val {int}
     * @return {int} 0-val
     */
    nextLowerInt(val) {
        return this.nextInt() % val
    },
    /**
     *
     * @param min {int}
     * @param max {int}
     * @return {int} min-max
     */
    nextBetweenInt(min, max) {
        return this.nextInt() % (max - min) + min
    }
}

export default Random
