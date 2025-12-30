//
//  Drv_OrderSingleCell.m
//  BJPinChe
//
//  Created by Kimoc on 14-8-18.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//  车主单次订单Cell

#import "Drv_OrderSingleCell.h"
#import "Drv_OrderMgrViewController.h"
#import "OrderCellButton.h"
#import "InsuranceModal.h"
//
@implementation Drv_OrderSingleCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)awakeFromNib
{
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}


- (void) initData : (STOrderInfo *)data parent:(id)parent
{
    mOrderInfo = data;
    mParent = parent;


    [_lblStartPos setText:mOrderInfo.startPos];
    [_lblEndPos setText:mOrderInfo.endPos];
    //[]
    // set user image
    //_imgUser
    
    // set sex image & change age color according to sex
    //_imgSex
    //if ([mOrderInfo.sex isEqualToString:SEX_MALE]) {
	[_imgUser setImageWithURL:[NSURL URLWithString:mOrderInfo.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
    if (mOrderInfo.sex == SEX_MALE) {
        _lblAge.textColor = MYCOLOR_GREEN;
    }
    [_lblAge setText:[NSString stringWithFormat:@"%d", mOrderInfo.age]];
    
    [_lblName setText:mOrderInfo.name];
    [_lblPrice setText:[NSString stringWithFormat:@"%.1f%@", mOrderInfo.price, DIAN]];
    [_lblType setText:mOrderInfo.type_desc];
    [_lblTime setText:mOrderInfo.start_time];
    [_lblContent setText:mOrderInfo.contents];
    [_lblState setText:mOrderInfo.state_desc];
    if(mOrderInfo.sex == 0) {
        [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
    }
    else {
        [_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
        _lblAge.textColor = MYCOLOR_GREEN;
    }
	[_btnOperate setBackgroundImage:nil forState:UIControlStateNormal];
    [_btnOperate setTitle:nil forState:UIControlStateNormal];
	switch (mOrderInfo.state) {
		case ORDER_STATE_DRIVER_ACCEPTED:
        case ORDER_STATE_PUBLISHED:
        {
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_start.png"] forState:UIControlStateNormal];
            _btnOperate.userInteractionEnabled =YES;
            break;
        }
        case ORDER_STATE_GRABBED:
        {
            [_btnOperate setHidden:YES];
//            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_start.png"] forState:UIControlStateNormal];
//            _btnOperate.userInteractionEnabled =YES;
            break;
        }
		case ORDER_STATE_STARTED:
        {   //执行中 待结算
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_BG"] forState:UIControlStateNormal];
            [_btnOperate setTitle:@"执行中" forState:UIControlStateNormal];
            break;
        }
        case ORDER_STATE_DRIVER_ARRIVED:
        {
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_BG"] forState:UIControlStateNormal];
            [_btnOperate setTitle:@"执行中" forState:UIControlStateNormal];
            break;
        }
        case ORDER_STATE_PASSENGER_GETON:
        {
			[_btnOperate setTitle:@"待 结 算" forState:UIControlStateNormal];
			[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_BG"] forState:UIControlStateNormal];
			_btnOperate.userInteractionEnabled =NO;
			break;
        }
		case ORDER_STATE_FINISHED:
		{
			[_btnOperate setHidden:YES];
			break;
		}
        case ORDER_STATE_PAYED:
        {
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_evalution.png"] forState:UIControlStateNormal];
            _btnOperate.userInteractionEnabled = YES;
            _btnOperate.hidden =NO;
			//[_btnOperate setTitle:@"去评价" forState:UIControlStateNormal];
            break;
        }
        case ORDER_STATE_EVALUATED:
        {
             _btnOperate.hidden = NO;
            _btnOperate.userInteractionEnabled =NO;//评价后按钮不能点击
            // set evaluate image by state
            switch (mOrderInfo.evaluated) {
                case EVAL_NOT:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment.png"] forState:UIControlStateNormal];
                    break;
                case EVAL_HIGH:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_good.png"] forState:UIControlStateNormal];
                    break;
                case EVAL_MEDIUM:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_medium.png"] forState:UIControlStateNormal];
                    break;
                case EVAL_LOW:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_low.png"] forState:UIControlStateNormal];
                    break;
                default:
                    break;
            }
            break;
        }
        case ORDER_STATE_CLOSED:
        {
            
            break;
        }
            
        default:
            break;
    }
}

- (IBAction)onClickedPerform:(id)sender
{
	TEST_NETWORK_RETURN
	
	[[CommManager getCommMgr] orderExecuteSvcMgr].delegate = self;
	
    Drv_OrderMgrViewController * ctrl = (Drv_OrderMgrViewController *)mParent;
    [ctrl onClickedPerform:mOrderInfo];
}

- (IBAction)onClickedDetailBtn:(id)sender {
    
    Drv_OrderMgrViewController * ctrl = (Drv_OrderMgrViewController *)mParent;
    
    [ctrl onClickedPerformInsuranceModal:mOrderInfo];
    
}


#pragma OrderExecuteSvcMgr Delegate Methods
- (void) duplicateUser:(NSString *)result
{
    NSLog(@"Detect duplicate user");
    
    [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
//	[mParent duplicateLogout];
}


- (void)evaluateOnceOrderPassResult:(NSString *)result Level:(int)level
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
		mOrderInfo.state = ORDER_STATE_EVALUATED;
		switch (level) {
			case EVAL_NOT:
				[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment.png"] forState:UIControlStateNormal];
				break;
			case EVAL_HIGH:
				[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_good.png"] forState:UIControlStateNormal];
				break;
			case EVAL_MEDIUM:
				[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_medium.png"] forState:UIControlStateNormal];
				break;
			case EVAL_LOW:
				[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_low.png"] forState:UIControlStateNormal];
				break;
			default:
				break;
		}
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


@end




