const SystemClock = {
    currentTimeMillis() {
        return new Date().getTime()
    },
    currentTimeSeconds() {
        return Math.floor(new Date().getTime() / 1000)
    }
}

export default SystemClock
