package com.skillLink.skillLink.Email;


public class VerificationCodeEmail implements EmailContent {
    String code ;
    public VerificationCodeEmail(String code){
        this.code = code;
    }

    @Override
    public String EmailBody() {
        return """
                <html>
                    <body style="font-family:Times New Roman, sans-serif; line-height: 2.0; font-size: 12px;">
                <h1> verification Code </h1>

                <p> This email is provided for registration on SkillLink.com </p>
                <p> please if this action has been not been performed by you ignore this email </p>
                <h2> your verification code is below:</h2>
                <br></br>
                <h1> <strong style="font-size: 24px; margin:10px 10px; padding:15px 15px;background:grey;
                color:purple;"> %s </strong> </h1>
                </body>
                </html>
                """.formatted(code);
                

    }
}
