import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from "axios";

const BASE_URL = import.meta.env.VITE_APP_BASE_URL;
const DEFAULT_HEADERS = {
  "Content-Type": "application/json",
};

export enum HttpMethod {
  Get = "GET",
  Post = "POST",
  Put = "PUT",
  Delete = "DELETE",
  Patch = "PATCH",
}

const createAxiosInstance = (base_url: string = BASE_URL): AxiosInstance => {
  return axios.create({
    baseURL: base_url,
    headers: DEFAULT_HEADERS,
  });
};

export const makeRequest = async (
  method: HttpMethod,
  url: string,
  data?: unknown,
  config?: AxiosRequestConfig,
  authenticated = false,
): Promise<AxiosResponse> => {
  const axiosInstance = createAxiosInstance();
  config = { ...config, withCredentials: authenticated };

  switch (method) {
    case HttpMethod.Get:
      return axiosInstance.get(url, config);
    case HttpMethod.Post:
      return axiosInstance.post(url, data, config);
    case HttpMethod.Put:
      return axiosInstance.put(url, data, config);
    case HttpMethod.Patch:
      return axiosInstance.patch(url, data, config);
    case HttpMethod.Delete:
      return axiosInstance.delete(url, config);
    default:
      throw new Error(`Invalid HTTP method: ${method}`);
  }
};
