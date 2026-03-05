package ug.daes.onboarding.model;

import jakarta.persistence.*;


@Entity
@Table(name = "simulated_boarder_control")
public class SimulatedBoarderControl {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_doc_number")
    private String idDocNumber;

    @Column(name = "selfie_image", columnDefinition = "TEXT")
    private String selfieImage;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdDocNumber() {
        return idDocNumber;
    }

    public void setIdDocNumber(String idDocNumber) {
        this.idDocNumber = idDocNumber;
    }

    public String getSelfieImage() {
        return selfieImage;
    }

    public void setSelfieImage(String selfieImage) {
        this.selfieImage = selfieImage;
    }

    @Override
    public String toString() {
        return "SimulatedBoarderControl{" +
                "id=" + id +
                ", idDocNumber='" + idDocNumber + '\'' +
                ", selfieImage='" + selfieImage + '\'' +
                '}';
    }
}

