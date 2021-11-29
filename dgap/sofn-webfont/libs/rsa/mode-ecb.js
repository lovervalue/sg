/*
CryptoJS v3.1.2
code.google.com/p/crypto-js
(c) 2009-2013 by Jeff Mott. All rights reserved.
code.google.com/p/crypto-js/wiki/License
*/
/**
 * Electronic Codebook block mode.
 */
CryptoJS.mode.ECB = (function () {
    var ECB = CryptoJS.lib.BlockCipherMode.extend();

    ECB.Encryptor = ECB.extend({
        processBlock: function (words, offset) {
            this._cipher.encryptBlock(words, offset);
        }
    });

    ECB.Decryptor = ECB.extend({
        processBlock: function (words, offset) {
            this._cipher.decryptBlock(words, offset);
        }
    });

    return ECB;
}());

/**
 * RSA����
 * @param data Ҫ���ܵ��ַ���
 * @param publicKey ��Կ����
 */
function encodeRSA(data,publicKey){
    var modulus = publicKey.modulus, exponent = publicKey.exponent;
    //���ݹ�Կϵ�����ɹ�Կ����
    var rsaKey = RSAUtils.getKeyPair(exponent, '', modulus);
    //rsa��key����
    var encodeKey=RSAUtils.encryptedString(rsaKey, data);
    return encodeKey;
}
/**
 * DES����
 * @param data json����
 * @param desKey ����key
 * @returns {*}
 */
function encodeDES(data,desKey){
    var dataStr=JSON.stringify(data);
    //des����
    var keyHex = CryptoJS.enc.Utf8.parse(desKey);
    var encrypted = CryptoJS.DES.encrypt(dataStr, keyHex, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    });
    //���ܺ������
    var encodeText =  encrypted.toString();
    return encodeText;
}
/**
 * DES����
 * @param data
 * @param desKey
 */
function decryptDES(data,desKey){
    var keyHex = CryptoJS.enc.Utf8.parse(desKey);
    // direct decrypt ciphertext
    var decrypted = CryptoJS.DES.decrypt({
        ciphertext: CryptoJS.enc.Base64.parse(data)
    }, keyHex, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    });
    var result=decrypted.toString(CryptoJS.enc.Utf8);
    return result;
}
