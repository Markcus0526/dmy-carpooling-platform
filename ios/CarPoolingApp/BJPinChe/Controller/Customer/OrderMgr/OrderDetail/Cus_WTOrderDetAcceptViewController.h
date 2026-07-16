//
//  Cus_WTOrderDetAcceptViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/2/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BMapKit.h"
#import "PasswordCheckModal.h"

@interface Cus_WTOrderDetAcceptViewController : SuperViewController <OrderSvcDelegate, BMKRouteSearchDelegate, BMKMapViewDelegate, pwdPopupDelegate>
{
    /********************** UI Controls **********************/
    // driver info control
    __weak IBOutlet UIImageView *              _imgUser;
    __weak IBOutlet UIImageView *              _imgSex;
    __weak IBOutlet UILabel *                  _lblAge;
    __weak IBOutlet UILabel *                  _lblName;
    __weak IBOutlet UIImageView *              _imgCar;
    
    __weak IBOutlet UILabel *                  _lblEvalPro;
    __weak IBOutlet UILabel *                  _lblServeCnt;
    __weak IBOutlet UIImageView *              _imgCarType;
    __weak IBOutlet UIImageView *              _imgCarBrand;
    __weak IBOutlet UIImageView *              _imgCarSubType;
    __weak IBOutlet UILabel *                  _lblCarColor;
    
    // order info control
    __weak IBOutlet UILabel *                  _lblStartPos;
    __weak IBOutlet UILabel *                  _lblEndPos;
    __weak IBOutlet UILabel *                  _lblOrderTime;
    __weak IBOutlet UILabel *                  _lblMidPoints;
    
   
    BMKRouteSearch *                    mRouteSearch;
    
    STDetailedCusOrderInfo *            mDetOrderInfo;
}


@property (weak, nonatomic) IBOutlet BMKMapView *  MapView;
@property (nonatomic, retain) STOrderInfo *     mOrderInfo;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedAccept:(id)sender;
- (IBAction)onClickedRefuse:(id)sender;

@end
