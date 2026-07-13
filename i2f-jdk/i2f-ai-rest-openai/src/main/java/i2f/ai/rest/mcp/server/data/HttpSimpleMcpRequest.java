package i2f.ai.rest.mcp.server.data;

import i2f.ai.rest.mcp.data.AppPayloadDto;
import i2f.mutator.BaseMutator;
import i2f.net.http.data.HttpHeaders;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/7/13 11:22
 * @desc
 */
@Data
@NoArgsConstructor
public class HttpSimpleMcpRequest implements BaseMutator<HttpSimpleMcpRequest> {
    protected HttpHeaders headers;
    protected AppPayloadDto payloadDto;
}
