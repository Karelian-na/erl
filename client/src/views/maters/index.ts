import { AxiosResponse } from "axios"
import { Result } from "common/Result"
import { UploadFile } from "element-plus"

export type MaterUploadFile = UploadFile & { response?: AxiosResponse<Result> }
