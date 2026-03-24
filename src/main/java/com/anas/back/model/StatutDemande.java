package com.anas.back.model;

public enum StatutDemande {
    EN_ATTENTE_MANAGER,   // Soumise, attend validation N1
    EN_ATTENTE_RH,        // Validée par Manager, attend validation N2
    APPROUVEE,            // Validée par RH
    REJETEE_MANAGER,      // Rejetée au niveau N1
    REJETEE_RH,           // Rejetée au niveau N2
    ANNULEE               // Annulée par l'employé
}