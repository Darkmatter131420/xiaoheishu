package com.darkmatter.framework.common.constant;

import java.time.format.DateTimeFormatter;

/**
 * @author: 犬小哈
 * @url: www.quanxiaoha.com
 * @date: 2024/5/5 15:40
 * @description: TODO
 **/
public interface DateConstants {

    /**
     * 年-月-日 时：分：秒
     */
    DateTimeFormatter Y_M_D_H_M_S_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 年-月-日
     */
    DateTimeFormatter Y_M_D_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 时：分：秒
     */
    DateTimeFormatter H_M_S_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * 年-月
     */
    DateTimeFormatter Y_M_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
}
