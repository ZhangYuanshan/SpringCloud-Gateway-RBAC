/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.quizhub.globalcommon.javabean.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Page is the result of Model.paginate(......) or Db.paginate(......)
 * @author lehr
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Pager<T>{

	private List<T> list;

	private Integer pageNo;

	private Integer pageSize;

	private Integer totalPage;

	private Long totalRow;

	public Pager(List<T> list, Integer pageNum,Integer pageSize,Integer pages, Long total)
	{
		this.list = list;
		pageNo = pageNum;
		this.pageSize = pageSize;
		totalPage = pages;
		totalRow = total;
	}

}

