import SwlContext from "./SwlContext";
import SwlHeader from "./SwlHeader";

/**
 * @return {SwlData}
 */
function SwlData(header = new SwlHeader(), parts = [], attaches = []) {
    /**
     * @type SwlHeader
     */
    this.header = header;
    /**
     * @type String[]
     */
    this.parts = parts
    /**
     * @type {string[]|null}
     */
    this.attaches = attaches
    /**
     * SwlContext
     */
    this.context = new SwlContext()
}


export default SwlData
