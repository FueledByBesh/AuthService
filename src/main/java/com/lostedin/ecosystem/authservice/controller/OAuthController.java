package com.lostedin.ecosystem.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/smthasdf/v1")
public class OAuthController {

    @GetMapping("/auth")
    protected String authorize(
            @RequestParam String client_id,
            @RequestParam String redirect_uri,
            @RequestParam String response_type,
            @RequestParam String scope,
            @RequestParam String state){
        return "authorized";
    }

    @PostMapping("")
    protected ResponseEntity<String> token(String body){
        return ResponseEntity.ok("token");
    }

    @PostMapping("/login")
    protected ResponseEntity<String> login(@RequestBody String body){
        return ResponseEntity.ok("logged in");
    }

    @PostMapping("/logout")
    protected ResponseEntity<String> logout(@RequestBody String body){
        return ResponseEntity.ok("logged out");
    }

    @PostMapping("/register")
    protected ResponseEntity<String> register(@RequestBody String body){
        return ResponseEntity.ok("registered");
    }


    String choose = "https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount?" +
            "response_type=code" +
            "&redirect_uri=https%3A%2F%2Flifeat.io%2Foauth%2Fgoogle%2Fcallback" +
            "&prompt=select_account%20consent" +
            "&state=%7B%22shouldGoBackToDesktop%22%3Afalse%7D" +
            "&client_id=460057837969-erbua701u2du3p881ikg72jaekuavj2o.apps.googleusercontent.com" +
            "&scope=openid%20https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email%20profile" +
            "&service=lso" +
            "&o2v=2" +
            "&flowName=GeneralOAuthFlow";
    String choose1 = "https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount?" +
            "response_type=code" +
            "&redirect_uri=https%3A%2F%2Flifeat.io%2Foauth%2Fgoogle%2Fcallback" +
            "&prompt=select_account%20consent" +
            "&state=%7B%22shouldGoBackToDesktop%22%3Afalse%7D" +
            "&client_id=460057837969-erbua701u2du3p881ikg72jaekuavj2o.apps.googleusercontent.com" +
            "&scope=openid%20https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email%20profile" +
            "&service=lso" +
            "&o2v=2" +
            "&flowName=GeneralOAuthFlow";

    String login = "https://accounts.google.com/v3/signin/identifier?" +
            "response_type=code" +
            "&redirect_uri=https%3A%2F%2Flifeat.io%2Foauth%2Fgoogle%2Fcallback" +
            "&prompt=select_account%20consent" +
            "&state=%7B%22shouldGoBackToDesktop%22%3Afalse%7D" +
            "&client_id=460057837969-erbua701u2du3p881ikg72jaekuavj2o.apps.googleusercontent.com" +
            "&scope=openid%20https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email%20profile" +
            "&service=lso" +
            "&o2v=2" +
            "&flowName=GeneralOAuthFlow" +
            "&continue=https%3A%2F%2Faccounts.google.com%2Fsignin%2Foauth%2Fconsent%3Fauthuser%3Dunknown%26part%3DAJi8hAOlRlZcIXQHvF2dLBTnQKhHXX8JSnaEUrCg1QOV2goHTPubYrpPDGSLdY_XUjPWTr0FGS4d-HOF_SRGG8iGIcJsDwbPLFWQmobqvIcmt2aHakpDNnaHAAyxDLkymBXYqSn8SRYFHJJdMHnPU8F0KzTWvt7Mfey57zCExg19UByZVf0B8pcTYGndrWF7AM_vqI43FIbosPa4e3ACZqzdGZLkyThKP2PVfvMqJbk99uZ-Yuc_JsXRM6Gg3PRVgolbZdF6jcgzWSHO9GxgLZBTBur7ksQpjNHw2Iy7b8LahP7345RDC7FiIeMiWr5dKhGrWndL1nqnkpnXUoYnNCsEoFl92049KET1Co5QEDnYps4OTgNLZd7yLcOZuFOLhsz3ipooQZurcsrNLiL48DMZz6ysuGxRIRcL9kXEk3adz-OTZw4sg6pJyT2tFv2vkru_ONub96O-fnJYYdPSaniyj1-gyxU3dA%26flowName%3DGeneralOAuthFlow%26as%3DS-1322956671%253A1749405466786829%26client_id%3D460057837969-erbua701u2du3p881ikg72jaekuavj2o.apps.googleusercontent.com%23";

    String register = "https://accounts.google.com/lifecycle/steps/signup/name?client_id=460057837969-erbua701u2du3p881ikg72jaekuavj2o.apps.googleusercontent.com&" +
            "continue=https://accounts.google.com/signin/oauth/consent?" +
            "authuser%3Dunknown%26part%3DAJi8hAOlRlZcIXQHvF2dLBTnQKhHXX8JSnaEUrCg1QOV2goHTPubYrpPDGSLdY_XUjPWTr0FGS4d-HOF_SRGG8iGIcJsDwbPLFWQmobqvIcmt2aHakpDNnaHAAyxDLkymBXYqSn8SRYFHJJdMHnPU8F0KzTWvt7Mfey57zCExg19UByZVf0B8pcTYGndrWF7AM_vqI43FIbosPa4e3ACZqzdGZLkyThKP2PVfvMqJbk99uZ-Yuc_JsXRM6Gg3PRVgolbZdF6jcgzWSHO9GxgLZBTBur7ksQpjNHw2Iy7b8LahP7345RDC7FiIeMiWr5dKhGrWndL1nqnkpnXUoYnNCsEoFl92049KET1Co5QEDnYps4OTgNLZd7yLcOZuFOLhsz3ipooQZurcsrNLiL48DMZz6ysuGxRIRcL9kXEk3adz-OTZw4sg6pJyT2tFv2vkru_ONub96O-fnJYYdPSaniyj1-gyxU3dA%26flowName%3DGeneralOAuthFlow%26as%3DS-1322956671%253A1749405466786829%26client_id%3D460057837969-erbua701u2du3p881ikg72jaekuavj2o.apps.googleusercontent.com%23&dsh=S-1643121439:1749405530521898&flowEntry=SignUp&flowName=GlifWebSignIn&" +
            "scope=openid+https://www.googleapis.com/auth/userinfo.email+profile&service=lso&TL=ALgCv6x2HwQCWzYHo6LA8Ai5kJuzW8j3zqNAhh-NioLlDdL00RQIxoWSK1UFGWTW";

    String access = "https://accounts.google.com/signin/oauth/id?" +
            "authuser=0&part=AJi8hAOyH-ntS8rTXIko3euZhlMWTqHFvXDQUiNCMIWUvOijQ418rat2La4iW3c_kajYCsC12W0RZfDYTStq2nwHFWXSZ3d_p3UHOPzJKblcYiFLvlifAX3zQOT6xB3FnIwhdA" +
            "0l2FMIgGHWCadCx7Qv62iMQZdKa59Zc-NSktpzKP4jTkVz-EoZfN8LtNWn3CI6pR2mmz5Ny4Wo7c1zgfKFdtiw1AP0B9u7wDeXKwRggJDR8w5svcPA8NAPhXQCF2hK9_9R6kzvXrkAzgxF9RqSKTFOCkJjY1YBX03RUehqbsf2tU93x8YNmd5C4vSQsf8JJol4Bxgciov9d3F4IBukH-_wqOSSBpqijgrnR0aQfSlOpTmzOKc7AwKOTROfxoQBjML5LMEM8WocGDhDWfSYRVX2dmPrqB6sQMaTpWpwK69awoTjVVYA20Qz8Ipj0Kk0FzMLJucZdPGZNYTKx-4OCi62sG4Iyh0X2dKnfpOcgay1uLxN8uccG07czfE_MZQ2kPhnjy2ow_hHPT_9i5sSVWgO-sQWpJ_KfdfJHesGseLrvg1XumHREKdmzX7BOgXdYJu2wbJJbDacC9-xM3Hf9m5GxyzqXXHu7rZITChxLa5dmhxbujjYQHLBv55yD97xpfTpr_u0x3JuPmgG4Rv5FZUSAg2Z-FnQnyHh3xsYdYUV0yngC8K22-JFmnrsitNRrzv5j174c8-PPXM8IlVrAZ6JvTXOCGpPYDoXLFsZgX_M6FM6ZmZ0LmMR4v5H-hUdd0WgvcTm-9tK3jX6E6D_hDV9os2Qaydx0qEtkFl8kIUPlfOzPW8MT7_opMVHo-B7aw8WTBhWYuDYaRyvNFjG1PVRNDGludv4JmnYwlqrNmpzsntpj3NtszB1E9_jWGc7avsEwaDBEvCrfLmSCytkOlYRo58W9NmODDSDd9U0CFtuMTbmr0IoO3CrQNpQPHOWsCdJmNKP93lF92nCY198MVRv60S5Vw" +
            "&flowName=GeneralOAuthFlow&as=S1779593582%3A1749406599240311&client_id=460057837969-erbua701u2du3p881ikg72jaekuavj2o.apps.googleusercontent.com&rapt=AEjHL4M-9yDrhkuXQOyAJkBgq2i2FPf2O9q5WFNRv2vcklQIj2gqFnfZeMY9JzE6bVRmxvMJ_FlvDUtmfJZk772FgR3W78W3zA#";

    String shit = "https://accounts.google.com/v3/signin/challenge/pwd" +
            "?TL=ALgCv6wAYYjCpmjeQgXWxrVYp1sbRAAVIWVFdFfpAHM6veYzLJmapvR1sMmD1rZG" +
            "&checkConnection=youtube%3A137&checkedDomains=youtube" +
            "&cid=2" +
            "&client_id=460057837969-erbua701u2du3p881ikg72jaekuavj2o.apps.googleusercontent.com" +
            "&continue=https%3A%2F%2Faccounts.google.com%2Fsignin%2Foauth%2Fconsent%3Fauthuser%3Dunknown%26part%3DAJi8hANmXwpBATRmyxL8Ix4hFtHSZClipDC61DA0i6DYp8bKS69cUIL7AXI9ZIx1MdysyGyjiWEg_BdC5ynnIbM2JZm8WOYam29a2xmnWzgqzM3H-nlUh8pMjlTriLcggiZOU9C62KONI1smF_2N4MLqze8YR77LwxLRorWdytHmOJZnDL96SDLqkFiaPJuJ9h_0Mp3Vq0wS-4JFwANvo8YN0f82onjQJRyRyNWvFVpUZTo0XeL1aBOw6WMileDIiLuGcLKTbFhWyzOWqeLgNWH6ODNtf8w-9V184fxP2By98lxlYlbGGE0Fhr61A3Sc_yJZPiHoNl5LUtaFARSVZjKKBiG0SlEpddMz2nuDic0Dy6Ow0e2cDGqs1SimUrcgwOe-y14pAn5XX2--va0CKVspgZgOMVF3ogV2F4jd-NPRHlZqEntEnPRyBW2QRs9muRT-zCiXRMyqhJjR7pkj1T0nMxmAAU8FTg%26flowName%3DGeneralOAuthFlow%26as%3DS-888355547%253A1749408773021579%26client_id%3D460057837969-erbua701u2du3p881ikg72jaekuavj2o.apps.googleusercontent.com%23&flowName=GeneralOAuthFlow&hl=ru&o2v=2&prompt=select_account+consent&redirect_uri=https%3A%2F%2Flifeat.io%2Foauth%2Fgoogle%2Fcallback&response_type=code&scope=openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+profile&service=lso&state=%7B%22shouldGoBackToDesktop%22%3Afalse%7D";


}
