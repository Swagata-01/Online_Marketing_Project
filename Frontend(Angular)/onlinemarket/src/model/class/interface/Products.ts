export interface IProductDTO{
    productid : number,
    name : String,
    description : String,
    avg_rating : number,
    subscription_count : number
}

export interface ISigninResponse{
    message : String,
    success : boolean,
    email : String
}

export type IUserIdResponse = number;


export interface IUserDetails {
  userID: number;
  firstName: string;
  lastName: string;
  email: string;
  nickName: string;
  address: string;
  photo: string;
  contactNumber: string;
  dateOfBirth: string;
  userRole: string;
  emailVerification: boolean;
  isActive: boolean;
  createdOn: string;
  updatedOn: string;
}




