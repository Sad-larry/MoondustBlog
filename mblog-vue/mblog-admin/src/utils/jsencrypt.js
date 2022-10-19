import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKw79JNltlUVYxBQ1tKPMUj61/8AtQ/c\n' +
  'CmO/6MH5Xi86Gmve0G2y7XFaB4+U+gSW5YpyHimGmJAN7NMpCg1+jy8CAwEAAQ==';

const privateKey = 'MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEArDv0k2W2VRVjEFDW\n' +
  '0o8xSPrX/wC1D9wKY7/owfleLzoaa97QbbLtcVoHj5T6BJblinIeKYaYkA3s0ykK\n' +
  'DX6PLwIDAQABAkAegPPdExuK0CHeZuLVIeUxrrOIh0CKEYeJiRiZGigzJsWg14vV\n' +
  'rGY1qXRasRbJHw6vggx1f5OHSEWxLTvgtYsxAiEA4zll20rWvVNJADhOye1btOA4\n' +
  'BLPLSPSWbD2C5KotejkCIQDCC8h0CiFsWZ/LaZe3kvHhoIdAOL88fPfd5GOrSgJ0\n' +
  'pwIhAIWpIuBvUohF8KA/fyFLDXIFnw4tEPyWW9HKETAfZucJAiAAsuIp6M9uAHSe\n' +
  '2uZ89r6APX+/L3Ug1qJd3jCtsTqJCQIgZDXLUK6FPmWBFGEnycY8N6c7wyBeck+Q\n' +
  '9S+0giJPx/o=';

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对数据进行加密
}

// 解密
export function decrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey) // 设置私钥
  return encryptor.decrypt(txt) // 对数据进行解密
}

