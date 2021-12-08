package UAC.IFRI.AA.PlanningClassroom.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MailContent {
    private String lastName;
    private String firstName;
    private String email;
    private String number;
    private String sexe;

    private String title;

    public MailContent(String lastName, String firstName, String email, String number, String sexe, String title) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.number = number;
        this.sexe = sexe;
        this.title = title;
    }
    public String generateEnseignantMSG()
    {
        return
                "<section style='background: url(\"https://cdn.pixabay.com/photo/2017/09/26/04/28/classroom-2787754_960_720.jpg\") no-repeat; background-origin: content-box;background-size: cover; height: 250px;'>"+
                    "<div style=\"background-color: #B4754C; color: white;\">" +
                        "<h2 style=\"text-align: center;\">"+title+"</h2>"+
                    "</div>"+
                    "<div style=\"width: 50%; border: 1px solid green; margin: auto; opacity: 0.8; background-color: #fff;background-clip: border-box;border: 1px solid rgba(0, 0, 0, 0.125);border-radius: 8px;\">" +
                        "<div style=\"text-align: center; font-size: 20px; text-decoration: underline;\">Bonjour Mr Ariel</div>"+
                            "Le suivant:" +
                        "<article style=\"margin-left: 70px;\">" +
                            "Nom : "+ lastName +"<br>"+
                            "Prénom : " + firstName + "<br>"+
                            "Email : " + email+ "<br>"+
                            "Sexe : " +sexe+ "<br>" +
                            "Telephone  : "+ number+ "<br>"+
                        "</article>" +
                            "S'est inscrit(e) sur votre Site en tant que <em>ENSEIGNANT</em>"+
                            "<br>"+
                            "Connaissez vous <em>DOSSOU Ariel</em> ?" +
                           "<a href=\"goole.com\">Confirmer</a>  ? ou <a href=\"google.com\">Supprimer</a>  ?"+
                    "</div>"+
                "</section>";
    }

    public String successConfirmMSG()
    {
        return
                "<section> "+
                    "<div style=\"background-color: #B4754C; color: white;\">"+
                        "<h2 style=\"text-align: center;\">"+title+"</h2>"+
                    "</div>"+
                    "<div>"+
                        "<div  style='display: inline-block; background: url(\"https://cdn.pixabay.com/photo/2017/09/26/04/28/classroom-2787754_960_720.jpg\") no-repeat; background-origin: content-box;background-size: cover; width: 25%; height: 320px; border-radius: 15px;'></div>"+
                        "<div  style='background-color:rgb(93, 90, 121, 0.2);float: right; width: 70%;padding: 6px; margin-left: 10px; font-size: 20px; font-family:  \"Lucida Sans Unicode\"; '>"+
                            "<div style=\"text-align: center; font-weight: bold; text-decoration: underline;\">Bonjour "+firstName+" "+lastName +"</div>"+
                            "<span>"+
                                "Vous avez soumis une demande d'inscription. Vous informations ont été analyser et confirmer avec succes"+

                                "Le suivant: "+
                                    "<article style=\"margin-left: 20px; font-size: 18px; font-family: Georgia, 'Times New Roman', Times, serif;\">"+
                                    "Nom : "+ lastName +"<br>"+
                                    "Prénom : " + firstName + "<br>"+
                                    "Email : " + email+ "<br>"+
                                    "Sexe : " +sexe+ "<br>" +
                                    "Telephone  : "+ number+ "<br>"+
                                    "</article>" +
                            "</span>"+
                            "est votre profil actuel. Vous pouvez modifier vos informations a  tout moment."+
                        "Nous vous remercions pour votre travail."+
                        "</div>"+
                    "</div>"+
                "</section>";
    }
}
