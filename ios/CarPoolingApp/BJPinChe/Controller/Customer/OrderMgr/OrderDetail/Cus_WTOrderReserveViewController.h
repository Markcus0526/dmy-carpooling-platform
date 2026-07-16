//
//  Cus_WTOrderReserveViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/3/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BMapKit.h"

@interface Cus_WTOrderReserveViewController : SuperViewController <BMKRouteSearchDelegate, BMKMapViewDelegate, OrderSvcDelegate>
{
    /********************** UI Controls **********************/
    // driver info control
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UILabel *                  _lblName;
    IBOutlet UIImageView *              _imgCar;
    
    IBOutlet UILabel *                  _lblEvalPro;
    IBOutlet UILabel *                  _lblServeCnt;
    IBOutlet UIImageView *              _imgCarType;
    IBOutlet UIImageView *              _imgCarBrand;
    IBOutlet UIImageView *              _imgCarSubType;
    IBOutlet UILabel *                  _lblCarColor;
    
    // order info control
    IBOutlet UILabel *                  _lblStartPos;
    IBOutlet UILabel *                  _lblEndPos;
    IBOutlet UILabel *                  _lblOrderTime;
    IBOutlet UILabel *                  _lblMidPoints;
    
    IBOutlet BMKMapView *               _MapView;
    BMKRouteSearch *                    mRouteSearch;
    
    STDetailedCusOrderInfo *            mDetOrderInfo;
}

@property (nonatomic, retain) STOrderInfo *     mOrderInfo;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedReserve:(id)sender;

@end
