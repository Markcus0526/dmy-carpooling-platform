//
//  Cus_WTOrderDetWaitViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/2/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BMapKit.h"

@interface Cus_WTOrderDetWaitViewController : SuperViewController <OrderSvcDelegate, BMKRouteSearchDelegate, BMKMapViewDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UILabel *                  _lblStartPos;
    IBOutlet UILabel *                  _lblEndPos;
    IBOutlet UILabel *                  _lblOrderTime;
    IBOutlet UILabel *                  _lblMidPoints;
    
    IBOutlet BMKMapView *               _MapView;
    
    BMKRouteSearch *                    mRouteSearch;
    STDetailedCusOrderInfo *            mDetOrderInfo;
}

@property (nonatomic, retain) STOrderInfo *     mOrderInfo;

// main control event
- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedCancelOrder:(id)sender;

@end
