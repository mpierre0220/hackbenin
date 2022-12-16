package com.example.textmapl.Activity;

import android.app.Activity;

import com.example.textmapl.modal.MessageText;
import com.example.textmapl.server.DeleteStatus;
import com.example.textmapl.server.TextMessage;

import java.util.ArrayList;
import java.util.List;
/*
    Classe pour la déclaration des variables statiques
 */
public class GlobalVar {
    //Dans un système de développement on aurait des serveurs de test et des serveurs de production
    //Dans le contexte du hackathon on n'a qu'un serveur de test
    public static String[] servers                                  = {"https://bancoregtest.com"};
    public static int SERVER                                        = 0; // 0 - test ; 1 - pwodiksyon
    public static int requeteType                                   = -1;
    public static int  identMessage                                 = -1;

    public static boolean  httpComplete                             = false;
    public static boolean  httpSucces                               = false;
    public static boolean  firstConnexion                           = false;

    public static String  userConnected                             = null;
    public static String  passwordConnected                         = null;

    public static List<Integer> listMessages                        = null;
    public static TextMessage textMessage                           = null;
    public static DeleteStatus deleteStatus                         = null;

    public final static int REQUETE_AJOUTER_MSG                     = 0;
    public final static int REQUETE_CHANGER_MOT_DE_PASSE            = 1;
    public final static int REQUETE_LISTER_ID_MESSAGES              = 2;
    public final static int REQUETE_MODIFIER_MESSAGE                = 3;
    public final static int REQUETE_SUPPRIMER_MESSAGE               = 4;
    public final static int REQUETE_LIRE_MESSAGE                    = 5;
    public final static int REQUETE_LISTER_TOUT_MESSAGE             = 6;

    public static Activity ActivityPrincipalThis;
    public static Activity ActivityLoginThis;

    public static ArrayList<TextMessage> arrayListMessageSever;

}
