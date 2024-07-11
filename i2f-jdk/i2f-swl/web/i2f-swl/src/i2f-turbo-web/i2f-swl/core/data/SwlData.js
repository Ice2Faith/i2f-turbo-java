import SwlContext from "./SwlContext";
import SwlHeader from "./SwlHeader";

/**
 * @return {SwlData}
 */
function SwlData(header = new SwlHeader(), parts = []) {
    /**
     * @type SwlHeader
     */
    this.header = header;
    /**
     * @type String[]
     */
    this.parts = parts
    /**
     * SwlContext
     */
    this.context = new SwlContext()
}


export default SwlData
