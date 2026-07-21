package i2f.springboot.ai.mcp.server.netty.impl;

import i2f.ai.rest.mcp.HttpSimpleMcpConstants;
import i2f.ai.rest.mcp.data.McpCallPayloadDto;
import i2f.ai.rest.mcp.server.HttpSimpleMcpServer;
import i2f.ai.rest.mcp.server.data.HttpSimpleMcpRequest;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.mutator.BaseMutator;
import i2f.net.http.data.HttpHeaders;
import i2f.resp.ApiResp;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/7/13 11:46
 * @desc
 */
@Data
@NoArgsConstructor
public class HttpSimpleMcpInBoundHandler extends SimpleChannelInboundHandler<FullHttpRequest> implements BaseMutator<HttpSimpleMcpInBoundHandler> {
    protected HttpSimpleMcpServer server;
    protected IJsonSerializer jsonSerializer = new Json2Serializer();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 1. 获取请求的 URI 和 Method
        String uri = request.uri();
        HttpMethod method = request.method();
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        String path = decoder.path();

        HttpSimpleMcpRequest mcpRequest = new HttpSimpleMcpRequest();
        mcpRequest.setHeaders(new HttpHeaders());
        mcpRequest.setPayloadDto(null);

        io.netty.handler.codec.http.HttpHeaders nettyHeaders = request.headers();
        Set<String> names = nettyHeaders.names();
        for (String name : names) {
            List<String> values = nettyHeaders.getAll(name);
            mcpRequest.getHeaders().add(name, values);
        }

        if (HttpSimpleMcpConstants.URL_PATH_GET_TOOLS.equals(path)) {
            if (HttpMethod.GET.compareTo(method) != 0) {
                responseInJson(ctx, ApiResp.error("bad request method!"));
                return;
            }
            ApiResp<List<ToolDefinition>> resp = server.getTools(mcpRequest);
            responseInJson(ctx, resp);
            return;
        }
        if (HttpSimpleMcpConstants.URL_PATH_CALL_TOOL.equals(path)) {
            if (HttpMethod.POST.compareTo(method) != 0) {
                responseInJson(ctx, ApiResp.error("bad request method!"));
                return;
            }
            ByteBuf contentBuf = request.content();
            String requestJson = contentBuf.toString(java.nio.charset.StandardCharsets.UTF_8);
            McpCallPayloadDto payloadDto = (McpCallPayloadDto) jsonSerializer.deserialize(requestJson, McpCallPayloadDto.class);
            mcpRequest.setPayloadDto(payloadDto);

            String content = payloadDto.getContent();
            ToolBaseCallRequest callRequest = (ToolBaseCallRequest) jsonSerializer.deserialize(content, ToolBaseCallRequest.class);

            Map<String, Object> contextMap = jsonSerializer.deserializeAsMap(payloadDto.getContext());

            try {
                ToolCallContextHolder.replaceAs(contextMap);
                ApiResp<?> resp = server.callTool(callRequest, mcpRequest);
                responseInJson(ctx, resp);
                return;
            } finally {
                ToolCallContextHolder.clear();
            }
        }
        ApiResp<?> resp = ApiResp.error("not route found: " + path);
        responseInJson(ctx, resp);

    }

    public void responseInJson(ChannelHandlerContext ctx, Object obj) {
        String responseJson = jsonSerializer.serialize(obj);

        ByteBuf responseBuf = ctx.alloc().buffer().writeBytes(responseJson.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                responseBuf
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, responseBuf.readableBytes());

        // 5. 写回响应并刷新
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
