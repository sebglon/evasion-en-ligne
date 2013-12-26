/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.api.data.booktravel;

import jodd.datetime.JDateTime;
import org.evasion.cloud.api.data.IAuthor;

/**
 *
 * @author sgl
 */
public interface IBook {

    String getTitle();

    void setTitle(String title);

    String getIntroduction();

    void setIntroduction(String introduction);

    JDateTime getStartDate();

    void setStartDate(JDateTime startDate);

    JDateTime getEndDate();

    void setEndDate(JDateTime startDate);

    IAuthor getAuthor();

    void setAuthor(IAuthor author);

}
