export default function verifySuccessResult() {
    client.assert(response.status === 200, '状态码不是 200');
    let r = response.body;
    client.assert(r.success === true, 'success 不为 true');
    let data = r.data;
    client.assert(data.name.length !== 0, '固件名称为空');
    client.assert(data.host.length !== 0, '主机名为空');
    client.assert(data.port > 0 && data.port < 65536, '端口号无效');
    client.assert(data.path.length !== 0, '路径为空');
    client.assert(data.tlsEnabled === true || data.tlsEnabled === false, 'tlsEnabled 类型不对');
}
