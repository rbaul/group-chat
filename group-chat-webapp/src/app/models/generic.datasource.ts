import { Page } from './page';
import { Observable } from 'rxjs';
import { PageableApiService } from '../utils/pageable-api-service';
import { PageableDataSource } from '../utils/pageable.datasource';


export class GenericDataSource<T> extends PageableDataSource<T> {

    constructor(private pageableApiService: PageableApiService<T>) {
        super();
    }

    getPageableContent(pageSize: number, pageIndex: number, sort: string[], filter: string): Observable<Page<T>> {
        return this.pageableApiService.search(pageSize, pageIndex, sort, filter);
    }

}
