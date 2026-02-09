package com.skillLink.skillLink.Email;

public class WelcomeEmail implements EmailContent {
    private  String name;
    public WelcomeEmail(String name){
        this.name = name;
    }

    @Override
    public String EmailBody() {
        return """
                <html>
                    <body style="font-family:Times New Roman, sans-serif; line-height: 2.0; font-size: 12px;">
                <h1> Welcome to SkillLink! </h1>
                <h3> Dear %s </h3>
               \s
                <p> We are thrilled to have you join our community\s
                of skilled professionals. At SkillLink, we are dedicated to connecting\s
                talented individuals like yourself with  exciting job opportunities in the environment. </p>
               \s
                <p> the sky is your limit, and we are here to support you every step of the way. </p>
                <br></br>
                <p> <i>Best regards,<i> </p>
                <p> <i>The SkillLink Team<i> </p>
                </body>
                <html>
               \s""".formatted(name);
    }
}
