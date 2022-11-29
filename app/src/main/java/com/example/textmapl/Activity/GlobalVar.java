package com.example.textmapl.Activity;

public class GlobalVar {
    //Dans un système de développement on aurait des serveurs de test et des serveurs de production
    //Dans le contexte du hackathon on n'a qu'un serveur de test
    public static String[] servers                                  = {"https://bancoregtest.com"};
    public static int SERVER                                        = 0; // 0 - test ; 1 - pwodiksyon
    public static int requeteType                                   = -1;

    public final static int REQUETE_AJOUTER_MSG                     = 0;
    public final static int REQUETE_CHANGER_MOT_DE_PASSE            = 1;
    public final static int REQUETE_LISTER_ID_MESSAGES              = 2;
    public final static int REQUETE_MODIFIER_MESSAGE                = 3;
    public final static int REQUETE_SUPPRIMER_MESSAGE               = 4;
}
