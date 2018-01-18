package com.home.DongFangHongRpc.common.constants;

import java.util.Collection;

/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月18日 下午6:19:55
 */
public final class CollectionsUtil{
	public final static boolean isEmpty (final Collection<?> collection){
		return null == collection || collection.isEmpty();
	}
}
