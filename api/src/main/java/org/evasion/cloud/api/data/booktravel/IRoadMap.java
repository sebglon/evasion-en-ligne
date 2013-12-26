/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.evasion.cloud.api.data.booktravel;

import jodd.datetime.JDateTime;

/**
 *
 * @author sgl
 */
public interface IRoadMap {
    
    String getDescription();
    void setDescription(String description);
    JDateTime getStartDate();
    void setStartdate(JDateTime date);
    JDateTime getEndDate();
    void setEndDate(JDateTime date);
    IItinerary getItinerary();
    void setItineraty(IItinerary itinerary);
}
