import SecureProviderPresets from "../preset/SecureProviderPresets";

const SecureProvider = {
    asymmetricEncryptor: SecureProviderPresets.asymmetricEncryptor_SM2,
    symmetricEncryptor: SecureProviderPresets.symmetricEncryptor_SM4,
    messageDigester: SecureProviderPresets.messageDigester_SM3
}
export default SecureProvider
